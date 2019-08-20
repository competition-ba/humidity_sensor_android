package com.competitionba.humiditysensor;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class StaticVarHolder {
    public static byte[] msg;
    public static int msglen;
    public static boolean isNFCActivityOpened;
    public static CH34xUARTDriver driver;//需要将CH34x的驱动类写在APP类下面，使得帮助类的生命周期与整个应用程序的生命周期是相同的
    public static byte[] getAvailMsg(){
        byte[] ret = new byte[msglen];
        System.arraycopy(msg, 0, ret, 0, msglen);
        return ret;
    }
}
