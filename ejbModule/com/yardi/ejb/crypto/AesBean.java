package com.yardi.ejb.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.ejb.Stateless;

@Stateless
public class AesBean implements Aes {
	public static final int TAG_LENGTH_IN_BITS = 128;
	public static final int IV_LENGTH_IN_BYTES = 12;
	public static final int SECRET_KEY_LENGTH_IN_BYTES = 32;
	public static final String ALGORITHM = "AES_256/GCM/NoPadding";

    public AesBean() {
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean AesBean() ");
		//debug
    }

	public byte[] decrypt(byte[] decryptParms) throws 	InvalidKeyException,
														InvalidAlgorithmParameterException, 
														IllegalBlockSizeException, 
														BadPaddingException, 
														NoSuchAlgorithmException, 
														NoSuchPaddingException 
	{
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean decrypt() 0000 ");
		//debug
    	ByteBuffer bb = ByteBuffer.wrap(decryptParms);
		byte[] iv = new byte[IV_LENGTH_IN_BYTES];
        byte[] secretKeyBytes = new byte[SECRET_KEY_LENGTH_IN_BYTES];
		bb.get(iv);
        bb.get(secretKeyBytes);
		byte[] cipherBytes = new byte[bb.remaining()];
		bb.get(cipherBytes);
		SecretKey secretKey = secretKeyFromBytes(secretKeyBytes);
		Cipher aesCipher = Cipher.getInstance(ALGORITHM);
		aesCipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_IN_BITS, iv));
		return aesCipher.doFinal(cipherBytes);
	}
	
	public byte[] encrypt(byte[] byteDataToEncrypt) throws 	NoSuchAlgorithmException, 
															NoSuchPaddingException, 
															InvalidKeyException,
															InvalidAlgorithmParameterException, 
															IllegalBlockSizeException, 
															BadPaddingException 
	{
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean encrypt() 0000 ");
		//debug
		SecretKey secretKey = inzSecretKey();
		byte[] iv = inzIV();
		Cipher aesCipher = Cipher.getInstance(ALGORITHM);
		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_IN_BITS, iv));
		byte[] cipherBytes = aesCipher.doFinal(byteDataToEncrypt);
		byte[] cipherTextWithIvSecretKey = ByteBuffer.allocate(iv.length + secretKey.getEncoded().length + cipherBytes.length)
                .put(iv)
                .put(secretKey.getEncoded())
                .put(cipherBytes)
                .array();
		return cipherTextWithIvSecretKey;
	}

	public byte[] extractCipherBytes(byte[] cipherTextWithIvSecretKey) {
		// store cipherBytes in db
		
        ByteBuffer bb = ByteBuffer.wrap(cipherTextWithIvSecretKey);
        byte[] iv = new byte[IV_LENGTH_IN_BYTES];
        byte[] secretKeyBytes = new byte[SECRET_KEY_LENGTH_IN_BYTES];
        bb.get(iv);
        bb.get(secretKeyBytes);
        byte[] cipherBytes = new byte[bb.remaining()];
        bb.get(cipherBytes);
        return cipherBytes;
	}
	
	public byte[] extractIvSecretKey(byte[] cipherTextWithIvSecretKey) {
		// store iv + SecretKey in JNDI
		
	    byte[] iv = new byte[IV_LENGTH_IN_BYTES];
	    byte[] secretKeyBytes = new byte[SECRET_KEY_LENGTH_IN_BYTES];
	    ByteBuffer bb = ByteBuffer.wrap(cipherTextWithIvSecretKey);
	    bb.get(iv);
	    bb.get(secretKeyBytes);
		byte[] ivSecretKey = ByteBuffer.allocate(iv.length + secretKeyBytes.length)
	            .put(iv)
	            .put(secretKeyBytes)
	            .array();
	    return ivSecretKey;
	}
	
	private byte[] inzIV() throws NoSuchAlgorithmException {
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean inzIV() 0000 ");
		//debug
		byte[] iv = new byte[IV_LENGTH_IN_BYTES];
		SecureRandom.getInstanceStrong().nextBytes(iv);
		return iv;
	}
	
	private SecretKey inzSecretKey() throws NoSuchAlgorithmException {
		// https://stackoverflow.com/questions/1925104/easy-way-to-store-restore-encryption-key-for-decrypting-string-in-java
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean inzSecretKey() 0000 ");
		//debug
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256, SecureRandom.getInstanceStrong());
		return keyGen.generateKey();
	}

	@PostConstruct
    private void postConstructCallback() {
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean postConstructCallback() ");
		//debug
    }
	
	private SecretKey secretKeyFromBytes(byte[] secretKeyBytes) {
		//debug
    	System.out.println("com.yardi.ejb.crypto.AesBean secretKeyFromBytes() 0000 ");
		//debug
		return new SecretKeySpec(secretKeyBytes, "AES");
	}
}
