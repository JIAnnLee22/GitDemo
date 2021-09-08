package com.jiannlee22.gitdemo.enumDemo;

import netscape.javascript.JSObject;

/**
 * @author admin
 */

public enum Hello {
    /**
     *
     */
    OOO("ooo"),
    /**
     *
     */
    HHH("hhh");
    private String info;

    private Hello(String info) {
        this.info = info;
    }

    public static void main(String[] args) {
        System.out.println(Hello.OOO.info);
        System.out.println(Hello.HHH.info);

    }
}
