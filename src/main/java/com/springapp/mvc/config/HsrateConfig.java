package com.springapp.mvc.config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yanzhao on 15/8/7.
 */
public class HsrateConfig {
    public static Date getZeroofToday()throws Exception{
        Date dt = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dts = df.format(dt);
        String [] ts = dts.split(" ");
        String time = ts[0] + " 00:00:00";
        return df.parse(time);
    }

    public static Date getStartOfDate(String dt)throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String start = dt + " 00:00:00";
        return df.parse(start);
    }

    public static Date getEndOfDate(String dt)throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String end = dt + " 59:59:59";
        return df.parse(end);
    }




    //worker
//    public static void main(String [] agrs)throws Exception{
//        while (true){
//            String response = HttpClientUtils.doGet(requestUrl, null);
//            System.out.println(response);
//            Thread.sleep(1*60*1000);
//        }
//    }

}
