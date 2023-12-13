package com.radcns.bird_plus.web.handler;

import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.google.common.io.BaseEncoding;
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.CreateRandomCodeUtil;
import com.radcns.bird_plus.util.KeyPairUtil;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.S3SseUtil;
import com.radcns.bird_plus.util.S3SseUtil.SSE_CustomerKeyRequest;
import com.radcns.bird_plus.util.S3SseUtil.SSE_CustomerKeyResponse;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.S3ApiException;
import com.radcns.bird_plus.util.properties.S3Properties;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.CreateMultipartUploadPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
@Component
public class S3Handler {

	@Autowired
	private S3AsyncClientBuilder s3AsyncClientBuilder;
	
	@Autowired
	private S3Properties s3Properties;
	
	@Autowired
	private S3Presigner.Builder s3PresignerBuilder;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	
    @Value("${s3.sse-c.key}")
	private String s3SseCKey;
	
    @Value("${s3.sse-c.slat}")
	private String s3SseCSlat;
	
    @Autowired
    private CreateRandomCodeUtil createRandomCodeUtil;
    
	public Mono<ServerResponse> generatePutObjectPresignedUrl(ServerRequest request){
		//var s3Client = s3AsyncClientBuilder.build();

		return accountService.convertRequestToAccount(request).flatMap(account -> 
			request.bodyToMono(SSE_CustomerKeyRequest.class)
			.filter(sseCustomerKeyRequest->{
				try {
					return sseCustomerKeyRequest.initVerify(KeyPairUtil.loadPublicKey(sseCustomerKeyRequest.getDataKey()));
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
					return false;
				}
			})
			.switchIfEmpty( Mono.error(new S3ApiException(Result._502)) )
			.filterWhen(sseCustomerKeyRequest -> 
				roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId( account.getId(), Long.valueOf(sseCustomerKeyRequest.getWorkspaceId()), Long.valueOf(sseCustomerKeyRequest.getRoomId()) )
			)
			.switchIfEmpty(Mono.error(new RoomException(Result._301)))
			.flatMap(sseCustomerKeyRequest -> getFileAuthRecord(sseCustomerKeyRequest))
			.flatMap(fileAuthRecord->{
				String fileName;
				if(fileAuthRecord.sseCustomerKeyRequest.getNewFileName() != null) {
					fileName = fileAuthRecord.sseCustomerKeyRequest.getNewFileName();
				}else {
					fileName = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + "_" +
						createRandomCodeUtil.createCode(new byte[32]) + "_" +
						fileAuthRecord.sseCustomerKeyRequest.getFileName();
				}
				PutObjectRequest putObjectReuqest = PutObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key("%s/%s/%s/%s/%s".formatted(
						fileAuthRecord.sseCustomerKeyRequest.getRoomId(),
						fileAuthRecord.sseCustomerKeyRequest.getWorkspaceId(),
						fileAuthRecord.sseCustomerKeyRequest.getUploadType(),
						fileAuthRecord.sseCustomerKeyRequest.getFileType(),
						fileName
					))
					.sseCustomerAlgorithm(ServerSideEncryption.AES256.toString())
					.sseCustomerKey(fileAuthRecord.base64Key)
					.sseCustomerKeyMD5(fileAuthRecord.base64Md)
					.build();
				
				PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
					.putObjectRequest(putObjectReuqest)
					.signatureDuration(Duration.ofHours(1))
					.build();
				
				URL presignedUrl = s3PresignerBuilder.build().presignPutObject(presignRequest).url();
				SSE_CustomerKeyResponse sseCustomerKeyResponse = SSE_CustomerKeyResponse.builder()
				.encryptionKey(fileAuthRecord.encBase64Key)
				.encryptionMd(fileAuthRecord.encBase64Md)
				.presignedUrl(presignedUrl)
				.newFileName(fileName)
				.build();
				return  ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response(Result._0, sseCustomerKeyResponse), ResponseWrapper.class);
			})
		);
	}
	public Mono<ServerResponse> generateGetObjectPresignedUrl(ServerRequest request){
		//var s3Client = s3AsyncClientBuilder.build();

		return accountService.convertRequestToAccount(request).flatMap(account -> 
			request.bodyToMono(SSE_CustomerKeyRequest.class)
			.filter(sseCustomerKeyRequest->{
				try {
					return sseCustomerKeyRequest.initVerify(KeyPairUtil.loadPublicKey(sseCustomerKeyRequest.getDataKey()));
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
					return false;
				}
			})
			.switchIfEmpty( Mono.error(new S3ApiException(Result._502)) )
			.filterWhen(sseCustomerKeyRequest -> 
				roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId( account.getId(), Long.valueOf(sseCustomerKeyRequest.getWorkspaceId()), Long.valueOf(sseCustomerKeyRequest.getRoomId()) )
			)
			.switchIfEmpty(Mono.error(new RoomException(Result._301)))
			.flatMap(sseCustomerKeyRequest -> getFileAuthRecord(sseCustomerKeyRequest))
			.flatMap(fileAuthRecord->{

				GetObjectRequest getObjectReuqest = GetObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key("%s/%s/%s/%s/%s".formatted(
						fileAuthRecord.sseCustomerKeyRequest.getRoomId(),
						fileAuthRecord.sseCustomerKeyRequest.getWorkspaceId(),
						fileAuthRecord.sseCustomerKeyRequest.getUploadType(),
						fileAuthRecord.sseCustomerKeyRequest.getFileType(),
						fileAuthRecord.sseCustomerKeyRequest.getFileName()
					))
					.sseCustomerAlgorithm(ServerSideEncryption.AES256.toString())
					.sseCustomerKey(fileAuthRecord.base64Key)
					.sseCustomerKeyMD5(fileAuthRecord.base64Md)
					.build();
				
				GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
					.getObjectRequest(getObjectReuqest)
					.signatureDuration(Duration.ofHours(1))
					.build();
				
				URL presignedUrl = s3PresignerBuilder.build().presignGetObject(presignRequest).url();
				SSE_CustomerKeyResponse sseCustomerKeyResponse = SSE_CustomerKeyResponse.builder()
				.encryptionKey(fileAuthRecord.encBase64Key)
				.encryptionMd(fileAuthRecord.encBase64Md)
				.presignedUrl(presignedUrl)
				.build();
				return  ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response(Result._0, sseCustomerKeyResponse), ResponseWrapper.class);
			})
		);
	}
	private static record FileAuthRecord(
			String base64Key,
			String base64Md,
			String encBase64Key,
			String encBase64Md,
			SSE_CustomerKeyRequest  sseCustomerKeyRequest
			){}
	private Mono<FileAuthRecord> getFileAuthRecord(SSE_CustomerKeyRequest  sseCustomerKeyRequest){
		Cipher encryptCipher;
		try {
			encryptCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
			encryptCipher.init(
				Cipher.ENCRYPT_MODE,
				KeyPairUtil.loadPublicKey(sseCustomerKeyRequest.getEncryptionKey()),
				KeyPairUtil.oaepParams
			);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			return Mono.error(new S3ApiException(Result._504));
		} catch(InvalidKeyException e) {
			return Mono.error(new S3ApiException(Result._505));
		}
		
		String keyString = sseCustomerKeyRequest.getCustomerProvidedKey();
		if(keyString == null) {
			keyString = "%s:room_id=%s,workspace_id=%s".formatted(s3SseCKey, sseCustomerKeyRequest.getRoomId(), sseCustomerKeyRequest.getWorkspaceId());
		}else {
			keyString = s3SseCKey + keyString; 
		}
		SecretKey key;
		byte[] mdDigestHash;
		try {
			key = S3SseUtil.generateKey(keyString, "{\"account_name\":\"%s\",\"slat\":\"%s\"}".formatted(sseCustomerKeyRequest.getAccountName(), s3SseCSlat));
			mdDigestHash = S3SseUtil.getMd5Digest(key.getEncoded());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			return Mono.error(new S3ApiException(Result._501));
		}
		String base64Key = BaseEncoding.base64().encode(key.getEncoded());
		String base64Md = BaseEncoding.base64().encode(mdDigestHash);
		String encBase64Key;
		String encBase64Md;
		try {
			encBase64Key = KeyPairUtil.encryptMessage(encryptCipher, base64Key);
			encBase64Md = KeyPairUtil.encryptMessage(encryptCipher, base64Md);
		}catch(IllegalBlockSizeException | BadPaddingException e) {
			return Mono.error(new S3ApiException(Result._506));
		}
		return Mono.just(
			new FileAuthRecord(base64Key, base64Md, encBase64Key, encBase64Md, sseCustomerKeyRequest)
		);
	}
}
