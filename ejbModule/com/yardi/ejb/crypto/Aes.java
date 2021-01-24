package com.yardi.ejb.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface Aes {
	byte[] decrypt(byte[] decryptParms) throws 	InvalidKeyException,
												InvalidAlgorithmParameterException, 
												IllegalBlockSizeException, 
												BadPaddingException, 
												NoSuchAlgorithmException, 
												NoSuchPaddingException;
	byte[] encrypt(byte[] byteDataToEncrypt) throws NoSuchAlgorithmException,
													NoSuchPaddingException, 
													InvalidKeyException,
													InvalidAlgorithmParameterException, 
													IllegalBlockSizeException, 
													BadPaddingException;
	byte[] extractCipherBytes(byte[] cipherTextWithIvSecretKey);  // store cipherBytes in db
	public byte[] extractIvSecretKey(byte[] cipherTextWithIvSecretKey);  // store iv + SecretKey in JNDI
}
