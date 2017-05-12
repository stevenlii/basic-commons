package com.paymoon.basic.commons.des;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.infinispan.commons.util.Base64;

import com.paymoon.basic.commons.core.MyProp;

/**
 * DES加密解密工具类
 * @author OneAPM
 *
 */
public class DESUtil {

	public static final String KEY_ALGORITHM = "DES";
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding ";
	public static final String USER_ENCRYPT_KEY = "user.encrypt.key";
	public static final String USER_ENCRYPT_KEY_DEFAULT = "kEHrDooxWHCWtfeSxvDvgqZq";
	
	public static String encrypt(String data, String key){
		if (StringUtils.isEmpty(data)) {
			return null;
		}
        Key deskey;
		try {
			deskey = keyGenerator(key);
		
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom random = new SecureRandom();
        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
        byte[] results = cipher.doFinal(data.getBytes());
        // 该部分是为了与加解密在线测试网站（http://tripledes.online-domain-tools.com/）的十六进制结果进行核对
//        for (int i = 0; i < results.length; i++) {
//            System.out.print(results[i] + " ");
//        }
//        System.out.println();
        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输 
        return Base64.encodeBytes(results);
		} catch (Exception e) {
		}
		return null;
    }
	public static String encrypt(String data){
		return encrypt(data,getKeyStoreConfiguration(USER_ENCRYPT_KEY));
	}
	
	 private static String getKeyStoreConfiguration(String string) {
		 String key = MyProp.getVariable(USER_ENCRYPT_KEY, USER_ENCRYPT_KEY_DEFAULT);
		return key;
	}
	public static String decrypt(String data, String key) {
		if (StringUtils.isEmpty(data)) {
			return null;
		}
	        Key deskey;
			try {
				deskey = keyGenerator(key);
			
	        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
	        //初始化Cipher对象，设置为解密模式
	        cipher.init(Cipher.DECRYPT_MODE, deskey);
	        // 执行解密操作
	        return new String(cipher.doFinal(Base64.decode(data)));
			} catch (Exception e) {
			}
			return null;
	    }
	public static String decrypt(String data){
		return decrypt(data,getKeyStoreConfiguration(USER_ENCRYPT_KEY));
	}
	
	private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }
	 
	//十六进制字符串转换成自己数组 
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }
    
    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
    
    public static void main(String[] args) throws Exception {
		String password = "123456";
		String key = "kEHrDooxWHCWtfeSxvDvgqZq";
		String enPassword = encrypt(password);
		System.out.println("-------加密密码--------"+enPassword);
		String dePassword = decrypt(enPassword, key);
		System.out.println("------解密密码---------"+dePassword);
	}
    
    
}
