package com.yardi.shared.userServices;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
 * Hash passwords for storage, and test passwords against password tokens.
 * 
 * Instances of this class can be used concurrently by multiple threads.
 *  
 * @author erickson
 * @see <a href="http://stackoverflow.com/a/2861125/3474">StackOverflow</a>
 */
public class PasswordAuthentication {
	/**
	   * The minimum recommended cost, used by default
	   */
	  public static final int DEFAULT_COST = 16;

	  /**
	   * Hash algorithm
	   */
	  private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

	  /**
	   * Key length
	   */
	  private static final int SIZE = 512;

	  /**
	   * Validate the number of iterations. The value passed must be be between 2 and 32. If <code>cost</code> satisfies the range constraint then the returned 
	   * number of iterations is 1 left shifted by <code>cost</code> positions.
	   * @param cost a value between 2 and 32 
	   * @return The number of iterations to use in the hash
	   */
	  private static int iterations(int cost)
	  {
	    if ((cost & ~0x1E) != 0)
	      throw new IllegalArgumentException("cost: " + cost);
	    return 1 << cost;
	  }

	  /**
	   * Derive the key using the key specification and specified hashing algorithm.
	   * @param password plain text password
	   * @param salt introduces randomness in the hash 
	   * @param iterations the number of iterations the hash algorithm should perform
	   * @return derived key
	   */
	  private static byte[] pbkdf2(char[] password, byte[] salt, int iterations)
	  {
	    KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
	    try {
	      SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
	      return f.generateSecret(spec).getEncoded();
	    }
	    catch (NoSuchAlgorithmException ex) {
	      throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
	    }
	    catch (InvalidKeySpecException ex) {
	      throw new IllegalStateException("Invalid SecretKeyFactory", ex);
	    }
	  }

	  /**
	   * The exponential computational cost of hashing a password
	   */
	  private final int cost;

	  public PasswordAuthentication()
	  {
	    this(DEFAULT_COST);
	  }

	  /**
	   * Create a password manager with a specified cost
	   * 
	   * @param cost the exponential computational cost of hashing a password, 0 to 30
	   */
	  public PasswordAuthentication(int cost)
	  {
		System.out.println("com.yardi.shared.userServices.PasswordAuthentication(int) 0002 "
				+ "\n"
				+ "DEFAULT_COST="
				+ DEFAULT_COST
				+ "\n"
				+ "cost="
				+ cost
		);
	    iterations(cost); /* Validate cost */
	    this.cost = cost;
	  }

	  /**
	   * Authenticate with a password and a stored password token.
	   * 
	   * @return true if the password and token match
	   */
	  public boolean authenticate(char[] password, String token)
	  {
		System.out.println("com.yardi.shared.userServices.PasswordAuthentication.authenticate() 0001 "
			+ "\n"
			+ "   password="
			+ new String(password)
			+ "\n"
			+ "   token="
			+ token
			);
		String t1 = token.substring(0, 2); 
		String t2 = token.substring(3);
	    int iterations = iterations(Integer.parseInt(t1));
	    byte[] hash = Base64.getUrlDecoder().decode(t2);
	    byte[] salt = new byte[SIZE / 8];
	    byte[] dk = new byte[SIZE / 8];
	    System.arraycopy(hash,           0, salt,           0, salt.length);
	    System.arraycopy(hash, salt.length,   dk,           0, dk.length);
	    byte[] check = pbkdf2(password, salt, iterations);
	    return Arrays.equals(dk, check);
	  }

	  /**
	   * Authenticate with a password in an immutable {@code String} and a stored 
	   * password token. 
	   * 
	   * @deprecated Use {@link #authenticate(char[],String)} instead.
	   * @see #hash(String)
	   */
	  @Deprecated
	  public boolean authenticate(String password, String token)
	  {
	    return authenticate(password.toCharArray(), token);
	  }

	  /**
	   * Hash a password for storage.
	   * 
	   * @return a secure authentication token to be stored for later authentication 
	 * @throws NoSuchAlgorithmException 
	   */
	  public String hash(char[] password) throws NoSuchAlgorithmException
	  {
		// SHA512 512 bits 64 bytes
		//   salt size = 64  bytes
		//   PBEKeySpec key size is 512 bits
		//   dk size = 64 bytes
		System.out.println("com.yardi.shared.userServices.PasswordAuthentication.hash() 0000 "
					+ "\n"
					+ "    DEFAULT_COST="
					+ DEFAULT_COST
					+ "\n"
					+ "    cost="
					+ cost
					+ "\n"
					+ "    password="
					+ new String(password)
		);
	    byte[] salt = new byte[SIZE / 8];
	    SecureRandom random = SecureRandom.getInstanceStrong();
	    random.nextBytes(salt);
	    byte[] dk = pbkdf2(password, salt, 1 << cost);
	    byte[] hash = new byte[salt.length + dk.length];
	    System.arraycopy(salt, 0, hash, 0, salt.length);
	    System.arraycopy(dk, 0, hash, salt.length, dk.length);
	    Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
		System.out.println("com.yardi.shared.userServices.PasswordAuthentication.hash() 0003 "
				+ "\n    "
				+ "hash="
				+ new String(cost + "$" + enc.encodeToString(hash))
				);
	    return cost + "$" + enc.encodeToString(hash);
	  }

	  /**
	   * Hash a password in an immutable {@code String}. 
	   * 
	   * <p>Passwords should be stored in a {@code char[]} so that it can be filled 
	   * with zeros after use instead of lingering on the heap and elsewhere.
	 * @throws NoSuchAlgorithmException 
	   * 
	   * @deprecated Use {@link #hash(char[])} instead
	   */
	  @Deprecated
	  public String hash(String password) throws NoSuchAlgorithmException
	  {
	    return hash(password.toCharArray());
	  }
}
