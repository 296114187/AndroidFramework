package com.voidid.core.baseclass;


import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.EventNames;
import com.voidid.core.event.EventManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAD {
    protected static Map<String, BaseAD> adMap = new HashMap<>();
    // 广告对象
    protected Object ad;
    // 广告单元ID
    protected String adUnitId;
    // 是否加载完成就展示
    protected boolean isLoadToShow = false;

    /**
     * 获取对象标识
     *
     * @return 广告位单元标识
     */
    public String getID() {
        return this.adUnitId;
    }

    /**
     * 设置对象标识
     *
     * @param adUnitId 广告位单元标识
     */
    protected void setID(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public void hide() {
    }

    public abstract void load();

    public abstract void show();

    public abstract void destroy();

    /**
     * 获取事件参数对象
     *
     * @return 参数
     */
    protected JSONObject getEventJSONObject() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("adUnitId", this.adUnitId);

        return jsonObj;
    }

    /**
     * 回调JS
     *
     * @param adType    广告类型
     * @param eventType 广告回调的事件类型
     * @param params    参数
     */
    protected void callBack(ADType adType, ADEventType eventType, JSONObject params) {
        String callBack = eventType.toString();
//        sendEvent(adType, eventType, params);
    }

    /**
     * 发送事件
     *
     * @param adType    广告类型
     * @param eventType 广告回调的事件类型
     * @param params    参数
     */
    protected void sendEvent(ADType adType, ADEventType eventType, Map<String, Object> params) {
        JSONObject data = new JSONObject();
        try {
            data.put("adUnitId", adUnitId);
            data.put("adType", adType);
            data.put("adEventType", eventType);
            for (String key : params.keySet()) { //遍历map
                data.put(key, params.get(key));
            }
            EventManager.emit(EventNames.EVENT_AD, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    protected void sendEvent(ADType adType, ADEventType eventType) {
        sendEvent(adType, eventType);
    }
}
