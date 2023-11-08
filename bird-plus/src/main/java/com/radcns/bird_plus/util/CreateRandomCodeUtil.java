package com.radcns.bird_plus.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.io.BaseEncoding;

@Component
public class CreateRandomCodeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateRandomCodeUtil.class);
	
	private int keyBit = 256;
	private byte[] keySize = new byte[32];
	private SecureRandom random = new SecureRandom();
	private KeyGenerator keyGen;
	private Cipher cipher;
	
	public CreateRandomCodeUtil() {
		try {
			this.keyGen = KeyGenerator.getInstance("AES");
			this.keyGen.init(this.keyBit);
			this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			Key key = this.keyGen.generateKey();
			this.cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		}	
	}
	
	public String createCode() {
		return createCode(this.keySize);
	}
	public String createCode(byte[] keySize) {
		random.nextBytes(keySize);
		return create(keySize);
	}
	
	public String createCode(String target) {
		//return create(target.getBytes());

		return BaseEncoding.base64().encode(target.getBytes());

	}
	private String create(byte[] b) {
		try {
			return BaseEncoding.base64().encode(cipher.doFinal(b));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	public static void main(String a[]) throws NoSuchAlgorithmException {
		//d29ya3NwYWNlSWQ9MSxyb29tSWQ9NSxhY2NvdW50SWQ9Ng==
		//d29ya3NwYWNlSWQ9MSxyb29tSWQ9NSxhY2NvdW50SWQ9Ng==
		System.out.println(new CreateRandomCodeUtil().createCode("workspaceId=1,roomId=5"));
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		md.update("workspaceId=1,roomId=5".getBytes());
		byte messageDigest[] = md.digest();
		StringBuffer hexString = new StringBuffer();
		for(int i = 0, len = messageDigest.length ; i < len ; i += 1) {
			//hexString.append(Integer.toString((messageDigest[i]&0xff) + 0x100, 16).substring(1));
			hexString.append(String.format("%02X", messageDigest[i]));
		}
		//3bad3985d05865faf3805c572c8c8986
		//3BAD3985D05865FAF3805C572C8C8986
		System.out.println(
				hexString.toString()
		);
	}
}
