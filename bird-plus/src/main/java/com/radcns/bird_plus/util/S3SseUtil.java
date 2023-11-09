package com.radcns.bird_plus.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.keygen.KeyGenerators;

import com.google.common.io.BaseEncoding;

public class S3SseUtil {
	
	private static SecureRandom random = new SecureRandom();
	
	public static IvParameterSpec generateIv() {
	    byte[] iv = new byte[16];
	    random.nextBytes(iv);
	    return new IvParameterSpec(iv);
	}
	
	public static SecretKey parseKey(String password, String salt)throws NoSuchAlgorithmException, InvalidKeySpecException {
	    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 256);
	    SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
	        .getEncoded(), "AES");
	    System.out.println(secret.toString());
	    System.out.println(secret.getEncoded().length);
	    
	    return secret;
	}
	public static SecretKey generateKey() throws NoSuchAlgorithmException {
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(256);
	    SecretKey key = keyGenerator.generateKey();
	    return key;
	}
	public static void main(String a[]) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//d29ya3NwYWNlSWQ9MSxyb29tSWQ9NSxhY2NvdW50SWQ9Ng==
		//d29ya3NwYWNlSWQ9MSxyb29tSWQ9NSxhY2NvdW50SWQ9Ng==
		//System.out.println(BaseEncoding.base64().encode("workspaceId=1,roomId=5".getBytes()));
		SecretKey test = S3SseUtil.parseKey("workspaceId=1,roomId=5", "account_name");
	    System.out.println(test.toString());
	    System.out.println(test.getEncoded().length);
	    System.out.println(new String(test.getEncoded()));
		//System.out.println(test);
		System.out.println(BaseEncoding.base64().encode(test.getEncoded()));
		MessageDigest md = MessageDigest.getInstance("MD5");
		//vWWLqsnrbGBCu61wwAImhqvlBb840GW/TxIyMoh4RfM=
		md.update(test.getEncoded());
		byte hash[] = md.digest();
		System.out.println(BaseEncoding.base64().encode(hash));
		
	}
}
