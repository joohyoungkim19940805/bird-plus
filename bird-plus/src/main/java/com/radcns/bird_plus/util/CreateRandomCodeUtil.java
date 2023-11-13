package com.radcns.bird_plus.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.io.BaseEncoding;

import jakarta.xml.bind.DatatypeConverter;

@Component
public class CreateRandomCodeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateRandomCodeUtil.class);
	
	private byte[] keySize = new byte[32];
	private SecureRandom random = new SecureRandom();
	private KeyGenerator keyGen;
	private Cipher cipher;
	
	public CreateRandomCodeUtil() {
		try {
			this.keyGen = KeyGenerator.getInstance("AES");
			this.keyGen.init(256);
			this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			Key key = this.keyGen.generateKey();
			this.cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		}	
	}
	public CreateRandomCodeUtil(int bit) {
		try {
			this.keyGen = KeyGenerator.getInstance("AES");
			this.keyGen.init(bit);
			this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			Key key = this.keyGen.generateKey();
			this.cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		}	
	}
	
	public String createCode(byte[] keySize) {
		random.nextBytes(keySize);
		return create(keySize);
	}
	
	public String createCode(String target) {
		//return create(target.getBytes());

		return create(target.getBytes());

	}
	private String create(byte[] b) {
		try {
			return BaseEncoding.base64().encode(cipher.doFinal(b));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	public static void main2(String a[]) throws NoSuchAlgorithmException {
		//d29ya3NwYWNlSWQ9MSxyb29tSWQ9NSxhY2NvdW50SWQ9Ng==
		//d29ya3NwYWNlSWQ9MSxyb29tSWQ9NSxhY2NvdW50SWQ9Ng==
		//System.out.println(BaseEncoding.base64().encode("workspaceId=1,roomId=5".getBytes()));
		String test = new CreateRandomCodeUtil().createCode("workspaceId=1,roomId=5");
		System.out.println(test);
		System.out.println(BaseEncoding.base64().encode(test.getBytes()));
		MessageDigest md = MessageDigest.getInstance("MD5");
		//vWWLqsnrbGBCu61wwAImhqvlBb840GW/TxIyMoh4RfM=
		md.update("workspaceId=1,roomId=5".getBytes());
		byte hash[] = md.digest();
		System.out.println(BaseEncoding.base64().encode(hash));
		
	}
	public static void main(String a[]) {
		System.out.println(
		new CreateRandomCodeUtil().createCode(new byte[32])
		);		System.out.println(
				new CreateRandomCodeUtil().createCode(new byte[32])
				);
	}
}
