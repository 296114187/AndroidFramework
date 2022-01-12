package com.voidid.umeng;

import com.voidid.core.AppConfig;
import com.voidid.core.AppGlobal;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.voidid.core.baseclass.BaseAnalytics;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.CurrencyType;
import com.voidid.core.enums.OrderState;
import com.voidid.core.enums.PlotResult;
import com.voidid.core.enums.PlotState;
import com.voidid.core.enums.PropState;
import com.voidid.core.jsb.DeviceManager;
import com.voidid.core.tools.DebugTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 友盟统计
 * https://developer.umeng.com/docs/119267/detail/118584#title-hkr-k8j-nss
 */
public class ModuleManager extends BaseAnalytics {
    private static final String TAG = ModuleManager.class.getName();

    /**
     * 预初始化SDK，必须在Application的onCreate中调用
     */
    @Override
    public void init() {
        super.init();
        DebugTool.d(TAG, "Umeng init");
        // 日志模式
        UMConfigure.setLogEnabled(AppConfig.ENABLE_DEBUG_LOG);
        // 预初始化
        UMConfigure.preInit(AppGlobal.getContext(), AppConfig.UMENG_APP_KEY, AppConfig.UMENG_APP_CHANNEL);
        // 手动页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        // 获取OAID
        UMConfigure.getOaid(AppGlobal.getContext(), DeviceManager::setOaid);
        // 初始化Umeng
        ModuleManager.initSDK();
    }

    /**
     * 初始化Umeng SDK，必须在用户同意授权之后调用
     */
    public static void initSDK() {
        DebugTool.d(TAG, "Umeng init");
        UMConfigure.init(AppGlobal.getContext(), AppConfig.UMENG_APP_KEY, AppConfig.UMENG_APP_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, "");
    }
    /**
     * 发送自定义事件
     *
     * @param eventName 事件名称
     * @param params    事件参数
     */
    @Override
    protected void onCustomEvent(String eventName, JSONObject params) throws JSONException {
        DebugTool.d(TAG, "[fl-api]sendEvent:" + eventName + " " + params.toString());
        //map对象
        Map<String, Object> data = new HashMap<>();
        //循环转换
        Iterator<String> it = params.keys();
        while (it.hasNext()) {
            String key = it.next();
            data.put(key, params.get(key));
        }
        MobclickAgent.onEventObject(AppGlobal.getContext(), eventName, data);
    }

    /**
     * 发送广告事件
     *
     * @param ad_type    广告类型
     * @param event_type 事件类型
     * @param params     事件参数
     */
    @Override
    protected void onAdEvent(ADType ad_type, ADEventType event_type, JSONObject params) throws JSONException {
        String eventName = ad_type + "_" + event_type;
        onCustomEvent(eventName, params);
    }

    /**
     * 发送支付事件
     *
     * @param state        支付状态
     * @param orderId      订单ID
     * @param amount       订单的总金额(单位：分)
     * @param currencyType 货币类型
     * @param params       事件参数
     */
    @Override
    protected void onPayEvent(OrderState state, String orderId, int amount, CurrencyType currencyType, JSONObject params) throws JSONException {
        String eventName = "Pay_" + state;
        params.put("orderId", orderId);
        params.put("amount", amount);
        params.put("currencyType", currencyType);
        onCustomEvent(eventName, params);
    }

    /**
     * 发送剧情事件
     *
     * @param name   剧情名称
     * @param state  剧情状态
     * @param result 剧情结果
     * @param params 事件参数
     */
    @Override
    protected void onPlotEvent(String name, PlotState state, PlotResult result, JSONObject params) throws JSONException {
        switch (state){
            case START: MobclickAgent.onPageStart(name); break;
            case END: MobclickAgent.onPageEnd(name); break;
        }
        onCustomEvent("plot_" + name + "_" + state.toString() + "_" + result.toString(), params);
    }

    /**
     * 发送道具事件
     *
     * @param state 状态使用、获得
     * @param count 数量
     * @param name  标记
     */
    @Override
    protected void onPropEvent(String name, PropState state, Double count) throws JSONException {
        onCustomEvent("prop_" + name + "_" + state.toString(), new JSONObject().put("count", count));
    }

    /**
     * 发送货币事件
     *
     * @param name  货币名称
     * @param state 状态使用、获得
     * @param count 事件类型
     */
    @Override
    protected void onCurrencyEvent(String name, PropState state, Double count) throws JSONException {
        onCustomEvent("currency_" + name + "_" + state.toString(), new JSONObject().put("count", count));
    }

    /**
     * 注册接口
     *
     * @param profileID 账户id
     * @param type      平台类型
     * @param name      昵称
     */
    @Override
    protected void onRegister(String profileID, String type, String name) {

    }

    /**
     * 登录接口
     *
     * @param profileID 账户id
     * @param type      平台类型
     * @param name      昵称
     */
    @Override
    protected void onLogin(String profileID, String type, String name) {
        MobclickAgent.onProfileSignIn(profileID);
    }

    /**
     * 登出接口
     */
    @Override
    protected void onLogout() {
        MobclickAgent.onProfileSignOff();
    }
}
