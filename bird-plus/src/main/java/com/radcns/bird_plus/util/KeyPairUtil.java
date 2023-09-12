package com.radcns.bird_plus.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


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
    
	private final String keyPairFileDir;    

	private final String keyPublicName;
    
	private final String keyPrivateName;
    
    private Path dirPath;
   
    private Path publicPath;
    
    private Path privatePath;
    
    @Getter
    private PublicKey publicKey;
    @Getter
    private PrivateKey privateKey;
    @Getter
    private KeyPair keyPair;
    
    public KeyPairUtil(String keyPairFileDir, String keyPublicName, String keyPrivateName) throws NoSuchAlgorithmException{
    	this.keyPairFileDir = keyPairFileDir;
    	this.keyPublicName = keyPublicName;
    	this.keyPrivateName = keyPrivateName;
    	
    	this.dirPath = Paths.get(this.keyPairFileDir);
    	this.publicPath = Paths.get(this.keyPairFileDir + "\\\\" + this.keyPublicName);
    	this.privatePath = Paths.get(this.keyPairFileDir + "\\\\" + this.keyPrivateName);
    	if(Files.notExists(this.publicPath, LinkOption.NOFOLLOW_LINKS) || Files.notExists(this.privatePath, LinkOption.NOFOLLOW_LINKS)) {
    		
            this.keyPair = this.createKey();
            
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            
    		try(
    				BufferedWriter publicWriter = Files.newBufferedWriter(this.publicPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    				BufferedWriter privateWriter = Files.newBufferedWriter(this.privatePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)
    		){
    			Files.createDirectories(this.dirPath);
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
    		this.publicKey = this.loadPublicKey();
    		this.privateKey = this.loadPrivateKey();
    		this.keyPair = new KeyPair(this.publicKey, this.privateKey);
    	} 	
    	
    }
    
    public PrivateKey loadPrivateKey() {
    	PrivateKey privateKey = null;
    	try {
    		
    		String str = Files.readAllLines(this.privatePath).get(1); 
    		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(KeyPairUtil.decoder.decode(str.getBytes()));
    		
    		privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    		
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return privateKey;
    }

    public PublicKey loadPublicKey() {
    	PublicKey publicKey = null;
    	try {
    		String str = Files.readAllLines(this.publicPath).get(1); 
    		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(KeyPairUtil.decoder.decode(str.getBytes()));
    		
    		publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
    		
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return publicKey;
    }

    
    public KeyPair createKey() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }
}
