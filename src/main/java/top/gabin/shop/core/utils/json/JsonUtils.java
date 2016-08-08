/**
 * Copyright (c) 2015 云智盛世
 * Created with JsonUtils.
 */
package top.gabin.shop.core.utils.json;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json工具类简单封装
 * @author linjiabin  on  15/8/29
 */
public class JsonUtils {

    public static String bean2Json(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(object);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T json2Bean(Class<T> clazz,String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
