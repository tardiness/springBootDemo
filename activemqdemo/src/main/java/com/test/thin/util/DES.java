package com.test.thin.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

public class DES {
    // 密钥
    private String secretKey = "";
    // 向量
    private String iv = "";
    // 加解密统一使用的编码方式
    private String encoding = "utf-8";

    public void setKey(String secretKey, String iv){
        this.secretKey = secretKey;
        this.iv = iv;
    }

    /**
     * CBC加密
     *
     * @param channel 明文字符串
     * @return 密文字符串
     * @throws Exception
     */


    public DES(String channel){
        // 密钥
        secretKey ="zghbwhszzyjyshxxjsyxgszz";
        // 向量
        iv = "15743628";
    }

    public DES(){
        // 密钥
        secretKey ="zghbwhszzyjyshxxjsyxgszz";
        // 向量
        iv = "15743628";
    }

    public  String EncodeCBC(String data) {

        Key deskey = null;
        DESedeKeySpec spec;
        try {
            spec = new DESedeKeySpec(secretKey.getBytes(encoding));

            SecretKeyFactory keyfactory = SecretKeyFactory
                    .getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes(encoding));
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(data.getBytes(encoding));
            return Base64.encode(encryptData);
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * CBC解 密
     *
     * @param data
     *            密文字符串
     * @return 明文字符串
     * @throws Exception
     */

    public  String DecodeCBC(String data) {
        Key deskey = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes(encoding));
            SecretKeyFactory keyfactory = SecretKeyFactory
                    .getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes(encoding));
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            byte[] decryptData = cipher.doFinal(Base64.decode(data));
            return new String(decryptData, encoding);
        } catch (Exception e) {
            return "";
        }
    }

}
