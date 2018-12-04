package com.cmcc.internalcontact.utils.converter;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * [JSON解析管理类]
 *
 * @author hjl
 * @version 1.0
 * @date 2017/4/7.
 */

public class JsonManager {

    /**
     * 将json字符串转换成java对象
     *
     * @param json json字符串
     * @param cls  java对象泛型Class定义
     * @return java对象
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json json字符串
     * @param type java对象泛型Type定义
     * @return java对象
     */
    public static <T> T jsonToBean(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json json字符串
     * @param cls  java List对象泛型
     * @return java List对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj bean对象
     * @return json字符串
     */
    public static String beanToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj bean对象
     * @return json字符串
     */
    public static Map<String, Object> beanToMap(Object obj) {
        return JSON.parseObject(beanToJson(obj));
    }

    /**
     * 将json转化成JSONObject
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToJsonObject(String json) {
        return JSON.parseObject(json);
    }
}
