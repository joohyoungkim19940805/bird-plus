package com.radcns.bird_plus.web.handler;

import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.google.common.io.BaseEncoding;
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.KeyPairUtil;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.S3SseUtil;
import com.radcns.bird_plus.util.S3SseUtil.SSE_CustomerKeyRequest;
import com.radcns.bird_plus.util.S3SseUtil.SSE_CustomerKeyResponse;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
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
	
	public Mono<ServerResponse> generatePutObjectPresignedUrl(ServerRequest request){
		//var s3Client = s3AsyncClientBuilder.build();

		return accountService.convertRequestToAccount(request).flatMap(account -> 
			request.bodyToMono(SSE_CustomerKeyRequest.class).flatMap(sseCustomerKeyRequest->{
				Cipher encryptCipher;

				try {
					sseCustomerKeyRequest.initVerify(KeyPairUtil.loadPublicKey(sseCustomerKeyRequest.getDataKey()));
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
					e.printStackTrace();
					return Mono.error(new S3ApiException(Result._502));
				}

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
					keyString = "room_id=%s,workspace_id:%s".formatted(sseCustomerKeyRequest.getRoomId(), sseCustomerKeyRequest.getWorkspaceId());
				}
				SecretKey key;
				byte[] mdDigestHash;
				try {
					key = S3SseUtil.generateKey(keyString, "{\"account_name\":\"%s\"}".formatted(sseCustomerKeyRequest.getAccountName()));
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
				PutObjectRequest putObjectReuqest = PutObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key("%s/%s/%s/%s".formatted(
						sseCustomerKeyRequest.getRoomId(),
						sseCustomerKeyRequest.getWorkspaceId(),
						sseCustomerKeyRequest.getUploadType(),
						sseCustomerKeyRequest.getFileName()
					))
					.sseCustomerAlgorithm(ServerSideEncryption.AES256.toString())
					.sseCustomerKey(base64Key)
					.sseCustomerKeyMD5(base64Md)
					.build();
				
				PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
					.putObjectRequest(putObjectReuqest)
					.signatureDuration(Duration.ofHours(1))
					.build();
				
				URL presignedUrl = s3PresignerBuilder.build().presignPutObject(presignRequest).url();
				SSE_CustomerKeyResponse sseCustomerKeyResponse = SSE_CustomerKeyResponse.builder()
				.encryptionKey(encBase64Key)
				.encryptionMd(encBase64Md)
				.presignedUrl(presignedUrl)
				.build();
				return  ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response(Result._0, sseCustomerKeyResponse), ResponseWrapper.class);
			})
		);
	}
	public Mono<ServerResponse> generateGetObjectPresignedUrl(ServerRequest request){
		//var s3Client = s3AsyncClientBuilder.build();

		return request.bodyToMono(Object.class).flatMap(e->{
				GetObjectRequest getObjectReuqest = GetObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key("test/test.png")
					.sseCustomerAlgorithm(ServerSideEncryption.AES256.toString())
					.sseCustomerKey("zCl8fl7i8t8q4IVZpQTp5QkIwR+S1RH2m3lpgnaMI+g=")
					.sseCustomerKeyMD5("WPgosOwwFY/pIMDVwcxnpg==")
					.build();
				
				GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
					.getObjectRequest(getObjectReuqest)
					.signatureDuration(Duration.ofHours(1))
					.build();
				var presignUrlReuqest = s3PresignerBuilder.build().presignGetObject(presignRequest);
				presignUrlReuqest.signedHeaders().putAll(Map.of(
					"x-amz-server-side-encryption-customer-algorithm", List.of("AES256"),
					"x-amz-server-side-encryption-customer-key", List.of("zCl8fl7i8t8q4IVZpQTp5QkIwR+S1RH2m3lpgnaMI+g="),
					"x-amz-server-side-encryption-customer-key-md5", List.of("WPgosOwwFY/pIMDVwcxnpg==")
				));
				URL presignedUrl = presignUrlReuqest.url();
				
				return  ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response(Result._0, presignedUrl), ResponseWrapper.class);
		});
	}
}
