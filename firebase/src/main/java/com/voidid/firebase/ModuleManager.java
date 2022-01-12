package com.voidid.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.voidid.core.AppGlobal;
import com.voidid.core.baseclass.BaseAnalytics;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.CurrencyType;
import com.voidid.core.enums.OrderState;
import com.voidid.core.enums.PlotResult;
import com.voidid.core.enums.PlotState;
import com.voidid.core.enums.PropState;
import com.voidid.core.tools.DebugTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Google FireBase
 *
 */
public class ModuleManager extends BaseAnalytics {
    private static final String TAG = ModuleManager.class.getName();
    private static FirebaseAnalytics mFireBaseAnalytics;
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private float adRemainRevenue = -1;

    @Override
    protected void onActivityCreate() {
        super.onActivityCreate();
        DebugTool.d(TAG, "Firebase init");
        sharedPref = AppGlobal.getMainActivity().getPreferences(Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPref.edit();
        mFireBaseAnalytics = FirebaseAnalytics.getInstance(AppGlobal.getContext());
        mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, new Bundle());
    }

    /**
     * 发送自定义事件
     *
     * @param event_id 事件id
     * @param params   事件参数
     */
    @Override
    protected void onCustomEvent(String event_id, JSONObject params) throws JSONException {
        Iterator<String> it = params.keys();
        Bundle bundle = new Bundle();
        while (it.hasNext()) {
            String key = it.next();
            try {
                bundle.putDouble(key, params.getDouble(key));
            } catch (JSONException e) {
                bundle.putString(key, params.getString(key));
            }
        }
        mFireBaseAnalytics.logEvent(event_id, bundle);
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
        Bundle bundle = new Bundle();
        switch (event_type) {
            case SHOW:
                DebugTool.i(TAG, "统计_发送广告展示事件：" + bundle.toString());
                double revenue = params.getDouble("revenue");
                bundle.putString(FirebaseAnalytics.Param.AD_FORMAT, ad_type.toString());
                bundle.putString(FirebaseAnalytics.Param.AD_PLATFORM, params.getString("network_name"));
                bundle.putDouble(FirebaseAnalytics.Param.VALUE, params.getDouble("revenue"));
                bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
                mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION, bundle);
                mFireBaseAnalytics.logEvent("Ad_Impression_Revenue", bundle);
                LogFirebaseAdRevenueEvent(revenue);
                break;
            case CLICKED:
                DebugTool.i(TAG, "统计_发送广告被点击事件：" + bundle.toString());
                mFireBaseAnalytics.logEvent("ad_click", bundle);
                break;
            case CLOSED:
                DebugTool.i(TAG, "统计_发送广告移除事件：" + bundle.toString());
                mFireBaseAnalytics.logEvent("ad_remove", bundle);
                break;
            case REWARDED:
                DebugTool.i(TAG, "统计_发送广告奖励事件：" + bundle.toString());
                mFireBaseAnalytics.logEvent("ad_reward", bundle);
                break;
        }
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
    protected void onPayEvent(OrderState state, String orderId, int amount, CurrencyType currencyType, JSONObject params) {
        if (state == OrderState.ORDER_PAY_SUCCESS) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, orderId);
            bundle.putString(FirebaseAnalytics.Param.AFFILIATION, "GooglePlay");
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, amount);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, currencyType.toString());
            bundle.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, "GooglePlay");
            mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle);
            DebugTool.i(TAG, "统计_订单支付：" + bundle.toString());
        }
    }

    /**
     * 发送剧情事件
     *
     * @param name   剧情名称
     * @param state  剧情状态
     * @param result 剧情名称
     * @param params 事件参数
     */
    @Override
    protected void onPlotEvent(String name, PlotState state, PlotResult result, JSONObject params) {
        Bundle bundle = new Bundle();
        if (state == PlotState.START) {
            bundle.putString(FirebaseAnalytics.Param.LEVEL_NAME, name);
            mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START, bundle);
            DebugTool.i(TAG, "统计_剧情开始：" + bundle.toString());
        } else if (state == PlotState.END) {
            bundle.putString(FirebaseAnalytics.Param.LEVEL_NAME, name);
            bundle.putLong(FirebaseAnalytics.Param.SUCCESS, result.hashCode());
            mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_END, bundle);
            DebugTool.i(TAG, "统计_剧情结束：" + bundle.toString());
        }
    }

    /**
     * 发送道具事件
     *
     * @param state 状态使用、获得
     * @param count 数量
     * @param name  标记
     */
    @Override
    protected void onPropEvent(String name, PropState state, Double count) {

    }

    /**
     * 发送货币事件
     *
     * @param name  货币名称
     * @param state 状态使用、获得
     * @param count 事件类型
     */
    @Override
    protected void onCurrencyEvent(String name, PropState state, Double count) {

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
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, type);
        bundle.putString("user_id", profileID);
        bundle.putString("nick", name);
        mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        DebugTool.i(TAG, "统计_登录：" + bundle.toString());
    }

    /**
     * 登出接口
     */
    @Override
    protected void onLogout() {

    }

    /**
     * 统计广告价值
     *
     * @param adRevenue 广告价值
     */
    private void LogFirebaseAdRevenueEvent(double adRevenue) {
        if (sharedPref == null) return;
        float previousAdsRevenue;
        if (this.adRemainRevenue == -1) {
            previousAdsRevenue = sharedPref.getFloat("FirebaseAdRevenue", 0); //App本地存储用于累计tROAS的缓存值
        } else {
            previousAdsRevenue = this.adRemainRevenue;
        }
        float currentAdsRevenue = (float) (previousAdsRevenue + adRevenue);//累加tROAS的缓存值
        //check是否应该发送ROAS事件
        if (currentAdsRevenue >= 0.01) {//如果超过0.01就触发一次tROAS taichi事件
            Bundle bundle = new Bundle();
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, currentAdsRevenue);//(Required)tROAS事件必须带Double类型的Value
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");//(Required)tROAS事件必须带Currency的币种，如果是USD的话，就写USD，如果不是USD，务必把其他币种换算成USD
            mFireBaseAnalytics.logEvent("Total_Ads_Revenue_001", bundle);
            sharedPreferencesEditor.putFloat("FirebaseAdRevenue", 0);//重新清零，开始计算
            this.adRemainRevenue = 0;
        } else {
            sharedPreferencesEditor.putFloat("FirebaseAdRevenue", currentAdsRevenue);//先存着直到超过0.01才发送
            this.adRemainRevenue = currentAdsRevenue;
        }
        sharedPreferencesEditor.commit();
    }
}
