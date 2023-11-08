package com.radcns.bird_plus.web.handler;

import java.net.URL;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.properties.S3Properties;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
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
	
	public Mono<ServerResponse> generatePresignedUrl(ServerRequest request){
		//var s3Client = s3AsyncClientBuilder.build();

		return request.bodyToMono(Object.class).flatMap(e->{
				PutObjectRequest putObjectReuqest = PutObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key("test/test.png")
					.sseCustomerAlgorithm(ServerSideEncryption.AES256.toString())
					.sseCustomerKey("d29ya3NwYWNlSWQ9MSxyb29tSWQ9NQ==")
					.sseCustomerKeyMD5("3BAD3985D05865FAF3805C572C8C8986")
					.build();
				
				PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
					.putObjectRequest(putObjectReuqest)
					.signatureDuration(Duration.ofHours(1))
					.build();
				URL presignedUrl = s3PresignerBuilder.build().presignPutObject(presignRequest).url();
				
				return  ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response(Result._0, presignedUrl), ResponseWrapper.class);
		});
	}
}
