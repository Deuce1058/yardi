package com.yardi.ejb.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.kosprov.jargon2.api.Jargon2;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;

/**
 * Session Bean implementation class Jargon2Bean
 */
@Stateless
public class Jargon2Bean {

    public Jargon2Bean() {
		System.out.println("com.yardi.ejb.crypto.Jargon2Bean() ");
    }
    
    private byte[] generateSalt() throws NoSuchAlgorithmException {
		System.out.println("com.yardi.ejb.crypto.Jargon2Bean.generateSalt() 0002 ");
    	byte[] salt = new byte[64];
	    SecureRandom random = SecureRandom.getInstanceStrong();
	    random.nextBytes(salt);
    	return salt;
	}

	public String hash(String password) throws NoSuchAlgorithmException {
		System.out.println("com.yardi.ejb.crypto.Jargon2Bean.hash() 0001 "
				+ "\n    "
				+ "password="
				+ password
				);
	    Jargon2.Hasher hasher = Jargon2.jargon2Hasher()
	            .type(Jargon2.Type.ARGON2id) // Argon2i or Argon2id
	            .memoryCost(65536)           // 64 MB try 131072 128mb, 262144 256mb
	            .timeCost(4)                 // Iterations
	            .parallelism(4)              // Number of threads
	            .saltLength(64)
	            .hashLength(64)
	            .salt(generateSalt())
	            .password(password.getBytes());
		return hasher.encodedHash();
	}

	@PostConstruct
    public void postConstructCallBack() {
		System.out.println("com.yardi.ejb.crypto.Jargon2Bean.postConstructCallBack() 0000 ");
    }
	
	public boolean verify(String password, String encodedHash) {
		System.out.println("com.yardi.ejb.crypto.Jargon2Bean.verify() 0003 "
				+ "/n    "
				+ "password="
				+ password
				+ "/n    "
				+ "encodedHash="
				+ encodedHash
				);
        return Jargon2.jargon2Verifier()
                .hash(encodedHash)
                .password(password.getBytes())
                .verifyEncoded();
    }
}
