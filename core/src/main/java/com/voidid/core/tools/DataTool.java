package com.voidid.core.tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataTool {

    /**
     * JSON字符串转Map对象
     *
     * @param jsonString JSON字符串
     * @return Map对象
     * @throws JSONException json解析失败
     */
    public static Map<String, Object> parseJsonToMap(String jsonString) throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonString);
        return jsonToMap(jsonObj);
    }

    /**
     * JSON字符串转Map对象
     *
     * @param jsonObj JSON
     * @return Map对象
     * @throws JSONException json解析失败
     */
    public static Map<String, Object> jsonToMap(JSONObject jsonObj) throws JSONException {
        Iterator<String> keys = jsonObj.keys();
        Map<String, Object> map = new HashMap<>();
        String key;

        while (keys.hasNext()) {
            key = keys.next();
            map.put(key, jsonObj.get(key));
        }

        return map;
    }

    /**
     * Map对象转JSON对象
     *
     * @param map JSON字符串
     * @return Map对象
     * @throws JSONException json解析失败
     */
    public static JSONObject mapToJson(Map<String, Object> map) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        Iterator<String> keys = map.keySet().iterator();
        String key;

        while (keys.hasNext()) {
            key = keys.next();
            jsonObj.put(key, map.get(key));
        }

        return jsonObj;
    }

    /**
     * JSON字符串转JSON对象
     *
     * @param jsonString JSON字符串
     * @return JSON对象
     * @throws JSONException JSON解析异常
     */
    public static JSONObject parseJson(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }

    /**
     * JSON对象转JSON字符串
     *
     * @param jsonObj 源数据
     * @return JSON字符串
     */
    public static String stringifyJson(JSONObject jsonObj) {
        return jsonObj.toString();
    }

    /**
     * 从JSON数据中获取String, 获取失败时返回空字符串
     *
     * @param jsonObj 原JSON数据对象
     * @param key     数据key
     * @return 字符串
     */
    public static String getStringToJson(JSONObject jsonObj, String key) {
        return getStringToJson(jsonObj, key, "");
    }

    /**
     * 从JSON数据中获取String
     *
     * @param jsonObj      原JSON数据对象
     * @param key          数据key
     * @param defaultValue 默认值
     * @return 字符串
     */
    public static String getStringToJson(JSONObject jsonObj, String key, String defaultValue) {
        try {
            return jsonObj.getString(key);
        } catch (JSONException ignored) {
            return defaultValue;
        }
    }

    /**
     * 从JSON数据中获取Int, 获取失败时返回空字符串
     *
     * @param jsonObj 原JSON数据对象
     * @param key     数据key
     * @return 字符串
     */
    public static int getIntToJson(JSONObject jsonObj, String key) {
        return getIntToJson(jsonObj, key, 0);
    }

    /**
     * 从JSON数据中获取int
     *
     * @param jsonObj      原JSON数据对象
     * @param key          数据key
     * @param defaultValue 默认值
     * @return 字符串
     */
    public static int getIntToJson(JSONObject jsonObj, String key, int defaultValue) {
        try {
            return jsonObj.getInt(key);
        } catch (JSONException ignored) {
            return defaultValue;
        }
    }

    /**
     * 从JSON数据中获取Double, 获取失败时返回空字符串
     *
     * @param jsonObj 原JSON数据对象
     * @param key     数据key
     * @return 字符串
     */
    public static double getDoubleToJson(JSONObject jsonObj, String key) {
        return getDoubleToJson(jsonObj, key, 0);
    }

    /**
     * 从JSON数据中获取String
     *
     * @param jsonObj      原JSON数据对象
     * @param key          数据key
     * @param defaultValue 默认值
     * @return 字符串
     */
    public static double getDoubleToJson(JSONObject jsonObj, String key, double defaultValue) {
        try {
            return jsonObj.getDouble(key);
        } catch (JSONException ignored) {
            return defaultValue;
        }
    }
}
