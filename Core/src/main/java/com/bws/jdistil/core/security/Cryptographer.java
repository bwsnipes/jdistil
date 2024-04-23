/*
 * Copyright (C) 2015 Bryan W. Snipes
 * 
 * This file is part of the JDistil web application framework.
 * 
 * JDistil is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JDistil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDistil.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bws.jdistil.core.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class Cryptographer {

	private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String DIGEST_ALGORITHM = "SHA-256";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String CHAR_SET_NAME = "UTF-8";
	
	private static final int KEY_SIZE = 128;

	private Cryptographer() {
		super();
	}
	 
	public static byte[] createSalt() throws NoSuchAlgorithmException {

    // Initialize return value
		byte[] salt = new byte[16];

		// Use secure random to obtain salt
    SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
    secureRandom.nextBytes(salt);
    
    return salt;
	}
	
	public static SecretKey createSecretKey(String value, byte[] salt, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException {

    // Create PBE key spec
    PBEKeySpec keySpec = new PBEKeySpec(value.toCharArray(), salt, iterations, KEY_SIZE);
    
    // Generate secret key
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    
    return secretKey;
	}
	
	public static byte[] digest(String value, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		// Convert value to bytes
		byte[] data = value.getBytes(CHAR_SET_NAME);
		
		MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
    messageDigest.update(salt);
    byte[] digest = messageDigest.digest(data);
    
    return digest;
	}
	
	
	public static byte[] encrypt(String value, SecretKey secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, 
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		// Convert value to bytes
		byte[] data = value.getBytes(CHAR_SET_NAME);

		// Create secret key spec using provided key
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		// Encrypt data
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] encryptedData = cipher.doFinal(data);

    return encryptedData;		
	}
	
	public static String decrypt(byte[] encryptedData, SecretKey secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, 
    	NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    
		// Create secret key spec using provided key
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		// Decrypt data
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] data = cipher.doFinal(encryptedData);
    
    // Convert to string value
    String decryptedData = new String(data, CHAR_SET_NAME);

    return decryptedData;		
  }
  
	public static void main(String[] args) {
		
		try {
			String password = "password";
			String value = "Value to process.";
			
			byte[] salt = Cryptographer.createSalt();
			String saltText = DatatypeConverter.printBase64Binary(salt);
			System.out.println("Salt: " + saltText);
			
			byte[] digest = Cryptographer.digest(value, salt);
			String digestText = DatatypeConverter.printBase64Binary(digest);
			System.out.println("Digest: " + digestText);
			
			SecretKey key = Cryptographer.createSecretKey(password, salt, 10000);
			String keyText = DatatypeConverter.printBase64Binary(key.getEncoded());
			System.out.println("Key: " + keyText);
			
			byte[] encrypted = Cryptographer.encrypt(value, key);
			String encryptedText = DatatypeConverter.printBase64Binary(encrypted);
			System.out.println("Encrypted: " + encryptedText);
			
	    byte[] encryptedBytes = DatatypeConverter.parseBase64Binary(encryptedText);
			
			String decrypted = Cryptographer.decrypt(encryptedBytes, key);
			System.out.println("Decrypted: " + decrypted);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
}
