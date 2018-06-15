package com.mmall.test;

import com.mmall.util.DateTimeUtil;
import org.apache.commons.net.ntp.TimeStamp;

import java.util.Date;
import java.util.Random;

public class OrderNoTest {

    public static void main(String[] args) {
        /*TimeStamp t = new TimeStamp(DateTimeUtil.strToDate("2018-05-22 09:50:48"));
        String utcString = t.toUTCString();
        long fraction = t.getFraction();*/
        long currentTimeMillis = System.currentTimeMillis();
        /*long time = t.getTime();
        System.out.println("utcString:"+utcString);
        System.out.println("fraction:"+fraction);
        System.out.println("currentTimeMillis:"+currentTimeMillis);
        System.out.println("time:"+time);
        TimeStamp t2 = new TimeStamp(DateTimeUtil.strToDate("2018-05-15 12:03:54"));
        System.out.println(163377618159L-Long.valueOf((time+"").substring(0,12)));
        System.out.println(159635697781L-Long.valueOf((t2.getTime()+"").substring(0,12)));
        System.out.println();*/
        hexStr(55);

    }

    public static void hexStr(int num){
        int result = num;
        String str = "";
        do{
            str = result%16+str;
            result = result/16;
        }while(result != 0);
        System.out.println(str);
    }

}
