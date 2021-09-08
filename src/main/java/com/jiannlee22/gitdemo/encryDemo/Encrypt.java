package com.jiannlee22.gitdemo.encryDemo;

import javax.crypto.Cipher;
import java.util.Base64;

public class Encrypt {
    public static void main(String[] args) {
        String str =
                "一二三四五六七八九十" +
                "一二三四五六七八九十" +
                "一二三四五六七八九十" +
                "一二三四五六七八九十" +
                "一二三四五六七八九十" +
                "一二三四五六七八九十" +
                "一二三四五六七八九十" +
                "一二三四五六七八九十";
        String xxx = "1234567890";
        try{
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
