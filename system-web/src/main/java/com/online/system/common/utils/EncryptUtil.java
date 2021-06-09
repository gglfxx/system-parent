package com.online.system.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加解密工具集。
 * 需要导入：commons-codec-1.10.jar 包。这个是apache的工具包。
 */
public class EncryptUtil {


    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * MD5摘要加密
     * eg: System.out.println(EncryptUtil.md5Encrypt("hello world!"));
     *
     * @param str 原始字符串
     * @return 加密后的字符串
     */
    public static String md5Encrypt(String str) {
        String returnStr = DigestUtils.md5Hex(str);
        return DigestUtils.md5Hex(returnStr);
    }
 
 
 
    /**
     * 对字符串做BASE64编码
     * eg: System.out.println(EncryptUtil.base64Encrypt("hello world! +-* / 中文"));
     *
     * @param str 原始字符串
     * @return 编码后的字符串
     */
    public static String base64Encrypt(String str) {
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }
 
 
    /**
     * 对byte[] 数组做base64 编码
     * eg: 	byte[] barr = IOUtil.readBinFile("d:/temp/b.jpg");
     * System.out.println(EncryptUtil.base64ByteEncrypt(barr));
     *
     * @param data byte[]数组
     * @return 编码后的字符串
     */
    public static String base64ByteEncrypt(byte[] data) {
        Base64 base64 = new Base64();
        data = base64.encode(data);
        String s = new String(data);
        return s;
    }
 
 
    /**
     * 对字符串做BASE64解码
     * eg: System.out.println(EncryptUtil.base64Decrypt(EncryptUtil.base64Encrypt("hello world! +-* / 中文")));
     *
     * @param str 编码后的字符串
     * @return 解码后的字符串
     */
    public static String base64Decrypt(String str) {
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s = new String(b);
        return s;
    }
 
 
    /**
     * 对字符串做Base64解码，解成byte[] 数组。可以通过此种方式解决Base64解码后中文乱码问题。
     * eg: 	byte[] barr = IOUtil.readBinFile("d:/temp/b.jpg");
     * String jpgStr = EncryptUtil.base64ByteEncrypt(barr);
     * IOUtil.writeBinFile("d:/temp/bb.jpg", EncryptUtil.base64ByteDecrypt(jpgStr));
     * eg:  String a = new String(EncryptUtil.base64ByteDecrypt("5oKo5aW977yMaGVsbG8gd29ybGQh"), "utf-8");
     *
     * @param str 编码后的字符串
     * @return 解码后的byte数组
     */
    public static byte[] base64ByteDecrypt(String str) {
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        return b;
    }
 
 
    /**
     * DES加密。对称加密算法，加密强度不高。一般现在计算机技术24小时左右可以破解。
     * 加密后返回的是做了Base64编码后的字符串。若直接用new String()的方式来转化，会出问题！无法解密回去。
     * eg: System.out.println(EncryptUtil.desEncrypt("helloworld您好，世界！^&*", "12345678"));
     *
     * @param plainStr 待加密的原始字符串
     * @param key      密钥，一般需要大于8位，为空或不足8位，会容错自动补齐
     * @return DES加密后并做了Base64编码后的字符串
     */
    public static String desEncrypt(String plainStr, String key) {
        try {
            //若是密钥为Null或者不足8位，自动补齐8位
            if (key == null) key = "hbbcfrom2014";
            if (key.length() < 8) key = key + "hbbcfrom2014";
            //准备加密
            Cipher cipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(keySpec));
            // 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length
            //  must be multiple of 8 when decrypting with padded cipher异常
            // 不能把加密后的字节数组直接转换成字符串，而应该是做Base64编码
            byte[] buf = cipher.doFinal(plainStr.getBytes());    //加密
            return new String((new Base64()).encode(buf));    //对结果进行Base64编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
 
    /**
     * DES解密
     * eg: String secretData = EncryptUtil.desEncrypt("helloworld您好，世界！^&*", "hehe");
     * System.out.println(EncryptUtil.desDecrypt(secretData, "hehe"));
     *
     * @param secretStr DES加密，并做Base64编码后的字符串
     * @param key       密钥，一般需要大于8位，为空或不足8位，会容错自动补齐
     * @return 解密后的字符串
     */
    public static String desDecrypt(String secretStr, String key) {
        try {
            //若是密钥为Null或者不足8位，自动补齐8位
            if (key == null) key = "hbbcfrom2014";
            if (key.length() < 8) key = key + "hbbcfrom2014";
            //准备解密
            Cipher cipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            keyFactory.generateSecret(keySpec);
            SecretKey keyObj = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, keyObj);
            //准备解密
            byte[] b = (new Base64()).decode(secretStr.getBytes());            //Base64反序列化为数组
            byte[] buf = cipher.doFinal(b);        //解密
            return new String(buf);                //组织成字符串形式返回
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String aesEncrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
            logger.warn("aes加密异常："+ex.getMessage());
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String aesDecrypt(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            logger.warn("aes解码异常："+ex.getMessage());
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            logger.warn("生成加密秘钥："+ex.getMessage());
        }
        return null;
    }

    /**
     * shiro MD5加密方式
     * @param credentials 加密串
     * @param hashIterations 散列的次数，比如散列两次，相当于md5(md5(""));
     * @return
     */
    public static String shiroMd5Encrypt(String credentials,int hashIterations){
        String hashAlgorithmName = "MD5";
        //String  password = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations).toHex();
        return null;
    }
    public static void main(String[] args) {

    }
}
