package com.jhx.common.util;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCBCUtil {

    public static void main(String[] args) {
        byte[] rawKey = stringToByte("a0593cf0997a0718ffc4a5736331f6b1e794878d5ba8cd92bc72b6de0e04ca34");
        System.out.println("rawKey : "+Arrays.toString(rawKey));
        byte[] encrypted = encrypt(rawKey, new byte[16], "我是唐！！".getBytes());
        System.out.println("encrypted : "+Arrays.toString(encrypted));
        System.out.println("encrypted : "+new String(encrypted));
        byte[] original = decrypt(rawKey, new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, encrypted);
        System.out.println("original : "+Arrays.toString(original));
        System.out.println("original : "+new String(original));
    }

    /**
     * 加密
     * 
     * @LastChanger t.ch
     * @time 2017-11-07 15:49
     * @param rawKey
     * @param iv
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] rawKey, byte[] iv, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(rawKey, "AES");
            
            IvParameterSpec ivSpec = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(content);
            return encrypted;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解密
     * 
     * @LastChanger t.ch
     * @time 2017-11-07 15:49
     * @param rawKey
     * @param iv
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] rawKey, byte[] iv, byte[] content){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(content);
            return original;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }
    }

    /**
     * 转字符串为byte数组
     * 
     * @LastChanger t.ch
     * @time 2017-11-07 15:50
     * @param hexString
     *            16进制字符串
     * @return
     */
    public static byte[] stringToByte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toLowerCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 16进制转byte用的
     * 
     * @LastChanger t.ch
     * @time 2017-11-07 15:50
     * @param c
     * @return
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }
    
    
    public static String bytesToHexString(byte[] src){   
        StringBuilder stringBuilder = new StringBuilder("");   
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < src.length; i++) {   
            int v = src[i] & 0xFF;   
            String hv = Integer.toHexString(v);   
            if (hv.length() < 2) {   
                stringBuilder.append(0);   
            }   
            stringBuilder.append(hv);   
        }   
        return stringBuilder.toString();   
    }  
}