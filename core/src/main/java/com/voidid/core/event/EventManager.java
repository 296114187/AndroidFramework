package com.voidid.core.event;

import android.annotation.SuppressLint;

import com.voidid.core.tools.DataTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件管理
 */
public class EventManager {
    @SuppressLint("StaticFieldLeak")
    private static EventManager _ins;

    private static EventManager ins() {
        if (_ins == null) _ins = new EventManager();
        return _ins;
    }

    private Map<String, ArrayList<EventFunction>> map = new HashMap<>();

    /**
     * 注册事件监听
     *
     * @param eventName 事件标记
     * @param func      事件回调函数
     */
    public static void on(String eventName, EventFunction func) {
        ArrayList<EventFunction> events = ins().map.get(eventName);
        if (events == null) {
            events = new ArrayList<>();
            ins().map.put(eventName, events);
        }
        events.add(func);
    }

    /**
     * 注销事件
     *
     * @param eventName 事件标记
     * @param func      事件回调函数
     */
    public static void off(String eventName, EventFunction func) {
        ArrayList<EventFunction> eventFunctions = ins().map.get(eventName);
        if (eventFunctions == null || eventFunctions.size() <= 0) return;
        for (EventFunction item : eventFunctions) {
            if (!item.equals(func)) continue;
            eventFunctions.remove(item);
            return;
        }
    }

    /**
     * 事件发送
     *
     * @param eventName 事件名
     * @param data      数据
     */
    public static void emit(String eventName, JSONObject data) {
        ArrayList<EventFunction> eventFunctions = ins().map.get(eventName);
        if (eventFunctions == null || eventFunctions.size() <= 0) return;
        for (int i = (eventFunctions.size() - 1); i >= 0; i--) {
            EventFunction func = eventFunctions.get(i);
            try {
                Map<String, Object> map = DataTool.jsonToMap(data);
                Map<String, Object> mapCopy = new HashMap<>();
                mapCopy.putAll(map);
                JSONObject jsonObject = DataTool.mapToJson(mapCopy);
                func.run(eventName, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 事件发送
     *
     * @param eventName 事件名
     */
    public static void emit(String eventName) {
        JSONObject data = new JSONObject();
        EventManager.emit(eventName, data);
    }
}
