package com.voidid.core.baseclass;


import static com.voidid.core.enums.EventNames.EVENT_AD;
import static com.voidid.core.enums.EventNames.EVENT_ANALYTICS;
import static com.voidid.core.enums.EventNames.EVENT_CURRENCY;
import static com.voidid.core.enums.EventNames.EVENT_PAY;
import static com.voidid.core.enums.EventNames.EVENT_PLOT;
import static com.voidid.core.enums.EventNames.EVENT_PROP;

import com.voidid.core.BaseModule;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.CurrencyType;
import com.voidid.core.enums.OrderState;
import com.voidid.core.enums.PlotResult;
import com.voidid.core.enums.PlotState;
import com.voidid.core.enums.PropState;
import com.voidid.core.event.EventFunction;
import com.voidid.core.event.EventManager;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseAnalytics extends BaseModule {
    protected BaseAnalytics() {
        eventListener();
    }

    /**
     * 注册事件
     */
    private void eventListener() {
        EventManager.on(EVENT_AD, AD_CallBack);
        EventManager.on(EVENT_PAY, Pay_CallBack);
        EventManager.on(EVENT_PLOT, Plot_CallBack);
        EventManager.on(EVENT_PROP, Prop_CallBack);
        EventManager.on(EVENT_ANALYTICS, CustomEvent_CallBack);
        EventManager.on(EVENT_CURRENCY, Currency_CallBack);
    }

    /**
     * 广告回调
     *
     * @param adType      广告类型
     * @param adEventType 广告事件类型
     */
    private final EventFunction AD_CallBack = (String eventName, JSONObject data) -> {
        ADType ad_type = (ADType) data.get("adType");
        ADEventType event_type = (ADEventType) data.get("adEventType");
        data.remove("adType");
        data.remove("adEventType");
        this.onAdEvent(ad_type, event_type, data);
    };

    /**
     * 自定义事件回调
     *
     * @param eventName 事件名称
     * @param ... 其它参数
     */
    private final EventFunction CustomEvent_CallBack = (String eventName, JSONObject data) -> {
        String event_id = data.getString("eventName");
        data.remove("eventName");
        onCustomEvent(event_id, data);
    };

    /**
     * 支付事件回调
     *
     *
     */
    private final EventFunction Pay_CallBack = (String eventName, JSONObject data) -> {
        OrderState play_state = (OrderState) data.get("state");
        String orderId = data.getString("orderId");
        CurrencyType currencyType = (CurrencyType) data.get("currencyType");
        int amount = data.getInt("amount");
        JSONObject params = data.getJSONObject("params");
        onPayEvent(play_state, orderId, amount, currencyType, params);
    };

    /**
     * 剧情事件回调
     */
    private final EventFunction Plot_CallBack = (String eventName, JSONObject data) -> {
        String name = data.getString("name");
        PlotState plot_state = (PlotState) data.get("state");
        PlotResult plot_result = (PlotResult) data.get("result");
        JSONObject params = data.getJSONObject("params");
        onPlotEvent(name, plot_state, plot_result, params);
    };

    /**
     * 道具事件回调
     */
    private final EventFunction Prop_CallBack = (String eventName, JSONObject data) -> {
        String name = data.getString("name");
        PropState useState = (PropState) data.get("state");
        double count = data.getDouble("count");
        onPropEvent(name, useState, count);
    };

    /**
     * 货币事件回调
     */
    private final EventFunction Currency_CallBack = (String eventName, JSONObject data) -> {
        PropState useState = (PropState) data.get("state");
        double count = data.getDouble("count");
        String name = data.getString("name");
        onCurrencyEvent(name, useState, count);
    };

    /**
     * 发送自定义事件
     *
     * @param event_name 事件id
     * @param params   事件参数
     */
    protected abstract void onCustomEvent(String event_name, JSONObject params) throws JSONException;

    /**
     * 发送广告事件
     *
     * @param ad_type    广告类型
     * @param event_type 事件类型
     * @param params     事件参数
     */
    protected abstract void onAdEvent(ADType ad_type, ADEventType event_type, JSONObject params) throws JSONException;

    /**
     * 发送支付事件
     *
     * @param state        支付状态
     * @param orderId      订单ID
     * @param amount       订单的总金额(单位：分)
     * @param currencyType 货币类型
     * @param params       事件参数
     */
    protected abstract void onPayEvent(OrderState state, String orderId, int amount, CurrencyType currencyType, JSONObject params) throws JSONException;

    /**
     * 发送剧情事件
     *
     * @param name   剧情名称
     * @param state  剧情状态
     * @param result 剧情结果
     * @param params 事件参数
     */
    protected abstract void onPlotEvent(String name, PlotState state, PlotResult result, JSONObject params) throws JSONException;

    /**
     * 发送道具事件
     *
     * @param state 状态使用、获得
     * @param count 数量
     * @param name  标记
     */
    protected abstract void onPropEvent(String name, PropState state, Double count) throws JSONException;

    /**
     * 发送货币事件
     *
     * @param name  货币名称
     * @param state 状态使用、获得
     * @param count 事件类型
     */
    protected abstract void onCurrencyEvent(String name, PropState state, Double count) throws JSONException;

    /**
     * 注册接口
     *
     * @param profileID 账户id
     * @param type      平台类型
     * @param name      昵称
     */
    protected abstract void onRegister(String profileID, String type, String name);

    /**
     * 登录接口
     *
     * @param profileID 账户id
     * @param type      平台类型
     * @param name      昵称
     */
    protected abstract void onLogin(String profileID, String type, String name);

    /**
     * 登出接口
     */
    protected abstract void onLogout();
}
