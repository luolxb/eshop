package com.soubao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


import org.springframework.util.StringUtils;

public final class TimeUtil {





    public TimeUtil() {
    }


        public static String creatUUID(String prefix){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String parse = simpleDateFormat.format(new Date());
            String randomString=String.format("%06d",new Random().nextInt(9999));
            String orderNo=prefix+parse+randomString;
            return orderNo;
        }

}
