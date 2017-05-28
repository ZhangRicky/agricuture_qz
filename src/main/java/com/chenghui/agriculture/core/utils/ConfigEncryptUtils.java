package com.chenghui.agriculture.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.crypto.hash.format.HexFormat;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

public class ConfigEncryptUtils {
	// KeyGenerator 提供对称密钥生成器的功能，支持各种算法
	private KeyGenerator keygen;
	// SecretKey 负责保存对称密钥
	private SecretKey deskey;
	// Cipher负责完成加密或解密工作
	private Cipher c;
	// 该字节数组负责保存加密的结果
	private byte[] cipherByte;

	public ConfigEncryptUtils() throws NoSuchAlgorithmException, NoSuchPaddingException {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
		keygen = KeyGenerator.getInstance("DES");
		// 生成密钥
		deskey = keygen.generateKey();
		// 生成Cipher对象,指定其支持的DES算法
		c = Cipher.getInstance("DES");
	}

	private static final StandardPBEStringEncryptor ENCRYPTOR = new StandardPBEStringEncryptor();

	static {
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		// Cipher.getInstance(this.algorithm, this.provider);
		// config.setAlgorithm("PBEWithMD5AndDES");
		config.setAlgorithm("PBEWithSHA1AndDESede");
		// 自己在用的时候更改此密码
		config.setPassword("config");

		// 获取jdk支持的密码学算法
		Provider[] providers = Security.getProviders();
		for (Provider provider : providers) {
			// System.out.println(provider.getInfo());
			// System.out.println(JSON.toJSONString(provider));
			Set<Provider.Service> services = provider.getServices();
			for (Service service : services) {
				System.out.println(service.getAlgorithm());
			}
		}

		ENCRYPTOR.setConfig(config);

	}

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
//		String plaintext = "root";
//		String ciphertext = ENCRYPTOR.encrypt(plaintext);
//		System.out.println(plaintext + " : " + ciphertext);
//		System.out.println(ciphertext + " : " + ENCRYPTOR.decrypt(ciphertext));
//
//		// ciphertext="royp2TvbUziD+86p1l8sxg==";
//		// System.out.println(ciphertext+" : "+ENCRYPTOR.decrypt(ciphertext));
//		plaintext = "sa";
//		ciphertext = ENCRYPTOR.encrypt(plaintext);
//		System.out.println(plaintext + " : " + ciphertext);
//		test002();
		
		
		String content = "java";
		String password="0123456789123456";
		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();  
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器   
        byte[] byteContent = content.getBytes("utf-8");  
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
        byte[] result = cipher.doFinal(byteContent);
        System.out.println(NumberConvert.bytesToHexString(result));//运行结果是：8fb2a3a086003f1eb1e35469f11f3f372809ef97d8f627e510c25249b6ac0299
	}

	private static void test002() {
		// 加密
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("password");
		String newPassword = textEncryptor.encrypt("123456");
		System.out.println(newPassword);
		// 解密
		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
		textEncryptor2.setPassword("password");
		String oldPassword = textEncryptor2.decrypt(newPassword);
		System.out.println(oldPassword);
		System.out.println("--------------------------");

		// 强加密算法需要去jdk官网下载jce.jar
		StrongTextEncryptor ste = new StrongTextEncryptor();
		// 加密
		ste.setPassword("password");
		String encyptedResult = ste.encrypt("123456");
		System.out.println("encyptedResult:" + encyptedResult);
		// 解密
		String dencyptedResult = ste.decrypt(encyptedResult);
		System.out.println(dencyptedResult);

	}

	public byte[] Encrytor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
		c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes();
		// 加密，结果保存进cipherByte
		cipherByte = c.doFinal(src);
		return cipherByte;
	}

	/**
	 * 对字符串解密
	 * 
	 * @param buff
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] Decryptor(byte[] buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
		c.init(Cipher.DECRYPT_MODE, deskey);
		cipherByte = c.doFinal(buff);
		return cipherByte;
	}
}