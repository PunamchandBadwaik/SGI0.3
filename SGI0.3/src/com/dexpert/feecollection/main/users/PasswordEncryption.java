package com.dexpert.feecollection.main.users;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/*import java.util.logging.Logger;*/

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PasswordEncryption {

	// for password encryption and decryption
	static Cipher ecipher;
	static Cipher dcipher;
	
	// 8-byte Salt
	static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3,
			(byte) 0x03 };
	static// Iteration count
	int iterationCount = 19;
	static String key = "Dspl_2014";
	static String plainStr = null;
	public static String encStr = null;

	static public void encrypt(String pwd) throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		/*Logger logger = Logger.getLogger("MyClass");*/

		KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, iterationCount);

		/*logger.info("encryption message 1 KeySpec :::" + keySpec);*/

		SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
		/*logger.info("encryption message 2 secrete key:" + secretKey);*/
		AlgorithmParameterSpec spec = new PBEParameterSpec(salt, iterationCount);
		/*logger.info("encryption message 3 AlgorithmParameterSpec :" + spec);*/
		try {
			ecipher = Cipher.getInstance(secretKey.getAlgorithm());
			
			ecipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
			String charSet = "UTF-8";
			byte[] in = null;
			in = pwd.getBytes(charSet);
			byte[] out = null;
			out = ecipher.doFinal(in);
			encStr = new sun.misc.BASE64Encoder().encode(out);
			/*logger.info("encryption message 4 encStr :" + encStr);*/
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		}

	}

	static public void decrypt(String pass) throws NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		String pwd = pass;
		/*Logger logger = Logger.getLogger("MyClass");*/
		KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, iterationCount);
		SecretKey secretKey = null;
		try {
			secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			AlgorithmParameterSpec spec = new PBEParameterSpec(salt, iterationCount);
			dcipher = Cipher.getInstance(secretKey.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
			byte[] enc = null;
			enc = new sun.misc.BASE64Decoder().decodeBuffer(pwd);
			byte[] utf8 = null;
			utf8 = dcipher.doFinal(enc);
			String charSet = "UTF-8";
			plainStr = new String(utf8, charSet);
			
			/*logger.info("Decryption message plain Text :" + plainStr);*/
		} catch (InvalidKeySpecException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

	}

}
