package com.soubao.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Md5Util {


    public static String md5Encode(String prefix, String pwd) {
        String pas = prefix + pwd;
        MessageDigest md = null;
        try {

            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.reset();
        String md5Pass = bytes2HexString(md.digest(pas.getBytes()));

        log.info(pwd+"加密==>{}",md5Pass);
        return md5Pass;
    }

    /**
     * 二进制转十六进制String
     *
     * @param bytes
     * @return
     */
    private static String bytes2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            //方法一
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16) {
                //当转换十进制，会忽略掉前面的"0"
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
            //方法二
            /*String s = Integer.toHexString(b[i] & 0xff);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);*/
        }
        return sb.toString();
    }

}
