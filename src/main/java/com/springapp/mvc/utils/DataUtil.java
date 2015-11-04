package com.springapp.mvc.utils;

import com.springapp.mvc.config.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanzhao on 15/8/11.
 */
public class DataUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static String [] getCurScData(String cd){
        String urlsh = "http://hq.sinajs.cn/list=sh";
        String urlsz = "http://hq.sinajs.cn/list=sz";

        String requestUrl = (cd.startsWith("0")? urlsz:urlsh) + cd;

        logger.info("get current stock data, request:{}", requestUrl);
        String response = null;
        int retry = 0;
        do{
            try{
                if(retry++>3){
                    break;
                }
                response = HttpClientUtils.doGet(requestUrl, null);
                logger.info("get current stock data, response:{}", response);
            }catch (Exception e){
                System.out.println("exception");
            }
        }while (response == null);

        Pattern p = Pattern.compile("\"(.+?)\"");
        Matcher matcher = p.matcher(response);
        String [] params ;
        if(matcher.find()) {
            params = matcher.group(1).split(",");
            return params;
        }else {
            return null;
        }
    }
}