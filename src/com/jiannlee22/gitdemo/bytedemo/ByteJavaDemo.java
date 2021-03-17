package com.jiannlee22.gitdemo.bytedemo;

import java.util.Arrays;

public class ByteJavaDemo {
    public static void main(String[] args) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) ((i + 190) & 0xFF);
        }
        for (byte aByte : bytes) {
            System.out.println((aByte & 0xff));
        }
    }
}
