package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by geely
 */
public class PropertiesUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties prop;

    static{
        String fileName = "mmall.properties";
        prop = new Properties();
        try {
            prop.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"utf-8"));
        } catch (IOException e) {
            LOG.error("配置文件读取异常"+e.getMessage());
        }

    }

    public static String getProperty(String key){
        String value = prop.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){
        String value = prop.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return defaultValue;
        }
        return value.trim();
    }

}
