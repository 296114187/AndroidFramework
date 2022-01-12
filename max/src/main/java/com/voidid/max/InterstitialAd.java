package com.voidid.max;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.voidid.core.baseclass.BaseAD;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;

import java.util.HashMap;
import java.util.Map;

public class InterstitialAd extends BaseAD implements MaxAdListener {
    public static String TAG = InterstitialAd.class.getName();

    private boolean isLoading = false;
    private boolean isPlayEnded = false;

    /**
     * 获取InterstitialAd广告对象
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static InterstitialAd getInstance(String placementID) {
        InterstitialAd ad = (InterstitialAd) InterstitialAd.adMap.get(placementID);

        if (ad == null) {
            ad = new InterstitialAd(placementID);
            ad.setID(placementID);
            InterstitialAd.adMap.put(placementID, ad);
        }

        return ad;
    }

    /**
     * 构造函数
     * @param placementID 广告ID
     */
    public InterstitialAd(String placementID) {
        DebugTool.d(TAG, "create()");
        this.ad = new MaxInterstitialAd(placementID, AppGlobal.getMainActivity());
        ((MaxInterstitialAd)this.ad).setListener(this);
        this.load();
    }

    /**
     * 广告是否就绪
     * @return boolean
     */
    public boolean isReady() {
        return ((MaxInterstitialAd)ad).isReady();
    }

    @Override
    public void load() {
        DebugTool.d(TAG, "load()");
        if (this.isLoading) {
            DebugTool.d(TAG, "插屏视频正在加载，请勿重复加载...");
            return;
        }
        this.isLoading = true;
        ((MaxInterstitialAd)ad).loadAd();
    }

    @Override
    public void show() {
        DebugTool.d(TAG, "show()");
        this.isPlayEnded = false;
        if (this.isReady()) {
            ((MaxInterstitialAd)ad).showAd();
            this.isLoadToShow = false;
        } else {
            // 广告未就绪，重新触发加载
            this.load();
            this.isLoadToShow = true;
        }
    }

    @Override
    public void destroy() {
        DebugTool.d(TAG, "destroy()");
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        DebugTool.d(TAG, "插屏视频加载成功");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad loaded.");
        sendEvent(ADType.INTERSTITIAL, ADEventType.LOADED, map);

        this.isLoading = false;
        if (this.isLoadToShow) {
            this.show();
        }
    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError adError) {
        DebugTool.e(TAG, "插屏视频加载失败:" + adError.getMessage());

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getMessage());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.INTERSTITIAL, ADEventType.LOAD_FAILED, map);
        this.isLoading = false;
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {
        DebugTool.d(TAG, "插屏视频展示");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed.");
        sendEvent(ADType.INTERSTITIAL, ADEventType.SHOWED, map);
    }

    @Override
    public void onAdHidden(MaxAd ad) {
        DebugTool.d(TAG, "插屏视频关闭");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "play ended.");
        map.put("isEnded", isPlayEnded);
        sendEvent(ADType.INTERSTITIAL, ADEventType.CLOSED, map);
        // 加载下一次的广告
        this.load();
    }

    @Override
    public void onAdClicked(MaxAd ad) {
        DebugTool.d(TAG, "插屏视频被点击");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad clicked");
        sendEvent(ADType.INTERSTITIAL, ADEventType.CLICKED, map);
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError adError) {
        DebugTool.d(TAG, "插屏视频发生错误");
        this.isLoading = false;

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getMessage());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.INTERSTITIAL, ADEventType.SHOW_FAILED, map);
        this.load();
    }
}
