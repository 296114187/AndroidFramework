package com.voidid.topon;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anythink.core.api.ATAdConst;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.splashad.api.ATSplashAd;
import com.anythink.splashad.api.ATSplashAdListener;
import com.anythink.splashad.api.IATSplashEyeAd;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.EEventNames;
import com.voidid.core.AppConfig;
import com.voidid.core.event.EventManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashAdShowActivity extends Activity implements  ATSplashAdListener {
    private static final String TAG = SplashAdShowActivity.class.getName();
    /** 广告实例 **/
    private ATSplashAd splashAd;
    /** 广告ID **/
    private String ad_id;
    /** 广告已经点击跳过 **/
    private boolean hasHandleJump = false;
    /** 是否可以跳转主页面，当开屏Activity处于后台时禁止跳转主页面 **/
    private boolean canJump;
    /** 广告容器 **/
    private FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //布局文件请参考Demo
        setContentView(R.layout.splash_ad_show);
        container = findViewById(R.id.splash_ad_container);
        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();

        Configuration cf = getResources().getConfiguration();
        int ori = cf.orientation;
        // 为了适配非全屏开屏广告，你应该指定container的layout param参数
        // 如果希望全屏显示，可以将以下代码的三个参数：0.9、0.85、0.85改成1

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        }
//        ATMediationRequestInfo atMediationRequestInfo = null;

        //Mintegral
//          atMediationRequestInfo = new MintegralATRequestInfo(appid, appKey, placement_id, unitId);
//          atMediationRequestInfo.setAdSourceId(mintegralTopOnAdSourceId);

        //腾讯广告（Tencent Ads）
//        atMediationRequestInfo = new GDTATRequestInfo(app_id, unit_id);
//        atMediationRequestInfo.setAdSourceId(gdtTopOnAdSourceId);

//        穿山甲（Pangle）
//        atMediationRequestInfo = new TTATRequestInfo(app_id, slot_id, personalized_template);
//        atMediationRequestInfo.setAdSourceId(pangleTopOnAdSourceId);

        //Sigmob
//        atMediationRequestInfo = new SigmobATRequestInfo(app_id, app_key, placement_id);
//        atMediationRequestInfo.setAdSourceId(sigmobTopOnAdSourceId);

        //百度
//        atMediationRequestInfo = new BaiduATRequestInfo(app_id, ad_place_id);
//        atMediationRequestInfo.setAdSourceId(baiduTopOnAdSourceId);

        //快手
//        atMediationRequestInfo  = new KSATRequestInfo(app_id, position_id);
//        atMediationRequestInfo.setAdSourceId(kuaishouTopOnAdSourceId);

        //Admob
//        atMediationRequestInfo = new AdmobATRequestInfo(app_id, unit_id, orientation);
//        atMediationRequestInfo.setAdSourceId(admobTopOnAdSourceId);

        //atMediationRequestInfo传入后，只针对首次请求开屏的情况生效，请详细阅读上方说明
//        splashAd = new ATSplashAd(this, AppConfig.SPLASH_ID, atMediationRequestInfo, this, 5000);

        ad_id = AppConfig.SPLASH_AD_UNIT_ID;
        splashAd = new ATSplashAd(this, ad_id, this);
        Map<String, Object> localMap = new HashMap<>();
        localMap.put(ATAdConst.KEY.AD_WIDTH, layoutParams.width);
        localMap.put(ATAdConst.KEY.AD_HEIGHT, layoutParams.height);
        splashAd.setLocalExtra(localMap);
        if (splashAd.isAdReady()) {
            splashAd.show(this, container);
        } else {
            splashAd.loadAd();
        }
        //打印出此广告位下的各个广告平台广告源的相关参数（必须已经初始化过SDK且调用ATSDK.setNetworkLogDebug(true);后才会打印）
        //请参考：https://docs.toponad.com/#/zh-cn/android/android_doc/android_sdk_splash_access?id=check_config
        ATSplashAd.checkSplashDefaultConfigList(this, ad_id, null);
    }





    @Override
    public void onAdLoaded(boolean isTimeout) {
        splashAd.show(this, container);
        try {
            JSONObject data = new JSONObject();
            data.put("adUnitId", ad_id);
            data.put("adType", ADType.SPLASH);
            data.put("adEventType", ADEventType.LOADED);
            EventManager.emit(EEventNames.EVENT_AD, data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAdLoadTimeout() {
        //本次加载超过时间超过fetchAdTimeout（默认为5s）没有广告返回，开发者可在此回调处理开屏加载超时逻辑
        Log.i(TAG, "SplashAd load timeout");
        jumpToMainActivity();
    }
    @Override
    public void onNoAdError(@NonNull AdError adError) {
        //注意：禁止在此回调中执行广告的加载方法进行重试，否则会引起很多无用请求且可能会导致应用卡顿
        //ADError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
        Log.e(TAG, "onNoAdError:" + adError.getFullErrorInfo());

        jumpToMainActivity();
    }

    @Override
    public void onAdShow(ATAdInfo adInfo) {
        //ATAdInfo可区分广告平台以及获取广告平台的广告位ID等
        //请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_sdk_callback_access?id=callback_info
    }

    @Override
    public void onAdClick(ATAdInfo adInfo) {
    }

    @Override
    public void onAdDismiss(ATAdInfo adInfo, IATSplashEyeAd splashEyeAd) {//v5.7.47开始，请使用此回调
        jumpToMainActivity();
    }

    /**
     * 前往主游戏界面
     */
    public void jumpToMainActivity() {
        try {
            JSONObject data = new JSONObject();
            data.put("adUnitId", ad_id);
            data.put("adType", ADType.SPLASH);
            data.put("adEventType", ADEventType.CLOSED);
            EventManager.emit(EEventNames.EVENT_AD, data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.i(TAG, "onNoAdError:前往主界面"+canJump );
        if (!canJump) {
            canJump = true;
            return;
        }
        if (!hasHandleJump) {
            hasHandleJump = true;
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            jumpToMainActivity();
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        canJump = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashAd != null) {
            splashAd.onDestory();
        }
    }
}
