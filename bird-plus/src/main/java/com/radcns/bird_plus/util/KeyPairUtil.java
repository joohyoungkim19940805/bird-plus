package com.radcns.bird_plus.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import com.google.common.io.BaseEncoding;

import lombok.Getter;

/**
 * 추후 s3기반으로 변경 예정
 */
public class KeyPairUtil {
	private static final String HEADER = "-----BEGIN %s KEY-----\n";
	private static final String FOOTER = "\n-----END %s KEY-----\n";
	
    private static final String PUBLIC = "PUBLIC";
    private static final String PRIVATE = "PRIVATE";
    
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    
    @Getter
    private PublicKey publicKey;
    @Getter
    private PrivateKey privateKey;
    @Getter
    private KeyPair keyPair;
    
    
	public static final OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
    
    
    public void saveAndSetVariableKeyPair(String keyPairFileDir, String keyPublicName, String keyPrivateName) throws NoSuchAlgorithmException{
    	
    	Path dirPath = Paths.get(keyPairFileDir);
    	Path publicPath = Paths.get(keyPairFileDir + "\\\\" + keyPublicName);
    	Path privatePath = Paths.get(keyPairFileDir + "\\\\" + keyPrivateName);
    	if(Files.notExists(publicPath, LinkOption.NOFOLLOW_LINKS) || Files.notExists(privatePath, LinkOption.NOFOLLOW_LINKS)) {
    		
            this.keyPair = KeyPairUtil.createKey();
            
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            
    		try(
    				BufferedWriter publicWriter = Files.newBufferedWriter(publicPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    				BufferedWriter privateWriter = Files.newBufferedWriter(privatePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)
    		){
    			Files.createDirectories(dirPath);
    			publicWriter.write(
					KeyPairUtil.HEADER.formatted(KeyPairUtil.PUBLIC) + 
					encoder.encodeToString(this.publicKey.getEncoded()) + 
					KeyPairUtil.FOOTER.formatted(KeyPairUtil.PUBLIC)
    			);
    			publicWriter.flush();
    			
    			privateWriter.write(
					KeyPairUtil.HEADER.formatted(KeyPairUtil.PRIVATE) + 
					encoder.encodeToString(this.privateKey.getEncoded()) + 
					KeyPairUtil.FOOTER.formatted(KeyPairUtil.PRIVATE)
    			);
    			privateWriter.flush();
    		} catch(IOException e) {
				e.printStackTrace();
			} catch(NullPointerException e){
				e.printStackTrace();
			}/* catch(AccessDeniedException e) {
				e.printStackTrace();
			}*/
    		
    	}else {
    		this.publicKey = KeyPairUtil.loadPublicKey(publicPath);
    		this.privateKey = KeyPairUtil.loadPrivateKey(privatePath);
    		this.keyPair = new KeyPair(this.publicKey, this.privateKey);
    	}
    }
    
    public static PrivateKey loadPrivateKey(Path privatePath) {
    	PrivateKey privateKey = null;
    	try {
    		
    		String str = Files.readAllLines(privatePath).get(1); 
    		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(KeyPairUtil.decoder.decode(str.getBytes()));
    		
    		privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    		
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return privateKey;
    }

    public static PrivateKey loadPrivateKey(String encodingPrivate) {
    	PrivateKey privateKey = null;
    	try {
    		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(KeyPairUtil.decoder.decode(encodingPrivate.getBytes()));
    		
    		privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    		
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return privateKey;
    }
    
    public static PublicKey loadPublicKey(Path publicPath) {
    	PublicKey publicKey = null;
    	try {
    		String str = Files.readAllLines(publicPath).get(1); 
    		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(KeyPairUtil.decoder.decode(str.getBytes()));
    		
    		publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
    		
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return publicKey;
    }
    public static PublicKey loadPublicKey(String encodingPublic) {
    	PublicKey publicKey = null;
    	try {
    		System.out.println("kjh test2222 <<<< ");
    		System.out.println(encodingPublic);
    		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(KeyPairUtil.decoder.decode(encodingPublic.getBytes()));
    		
    		publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
    		
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return publicKey;
    }

    
    public static KeyPair createKey() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }
    
    public static String encryptMessage(Cipher encryptCipher, String message) throws IllegalBlockSizeException, BadPaddingException {
    	byte b[] = encryptCipher.doFinal(message.getBytes());
    	return BaseEncoding.base64().encode(b);
    }
    
    public static void main(String a[]) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    	var k = new KeyPairUtil();
    	var pair = KeyPairUtil.createKey();
    	var privateK = pair.getPrivate();
    	var publicK = pair.getPublic();
    	String test = "test";
    	byte[] b = test.getBytes();
    	
    	Signature sig = Signature.getInstance("SHA256withRSA");
    	sig.initSign(privateK);
    	sig.update(b);
    	
    	System.out.println(new String(b));
    	
    	byte sb[] = sig.sign();
    	
    	String base = BaseEncoding.base64().encode(sb);
    	
    	System.out.println(base);
    	
    	Signature pubSig = Signature.getInstance("SHA256withRSA");
    	pubSig.initVerify(publicK);
    	pubSig.update(b);
    	
    	System.out.println(new String(b));
    	
    	System.out.println(pubSig.verify(BaseEncoding.base64().decode(base)));
    	
    	System.out.println(new String(b));
    }
}
