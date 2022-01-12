package com.voidid.topon;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.nativead.api.ATNative;
import com.anythink.nativead.api.ATNativeAdView;
import com.anythink.nativead.api.ATNativeEventListener;
import com.anythink.nativead.api.ATNativeNetworkListener;
import com.voidid.core.baseclass.BaseAD;
import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;

public class NativeAd extends BaseAD implements ATNativeNetworkListener, ATNativeEventListener {
    public static String TAG = NativeAd.class.getName();

    /**
     * 获取广告对象
     *
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static NativeAd getInstance(String placementID) {
        NativeAd ad = (NativeAd) NativeAd.adMap.get(placementID);

        if (ad == null) {
            ad = new NativeAd(placementID);
            ad.setID(placementID);
            NativeAd.adMap.put(placementID, ad);
        }

        return ad;
    }

    /**
     * 构造函数
     *
     * @param placementID 广告ID
     */
    public NativeAd(String placementID) {
        this.ad = new ATNative(AppGlobal.getContext(), placementID, this);
    }

    /**
     * 广告是否就绪
     *
     * @return isReady
     */
    public boolean isReady() {
        return ((ATNative) ad).checkAdStatus().isReady();
    }


    @Override
    public void load() {
        ((ATNative) ad).makeAdRequest();
    }

    @Override
    public void show() {

    }

    @Override
    public void destroy() {

    }


    @Override
    public void onNativeAdLoaded() {
        // 广告加载成功
    }

    @Override
    public void onNativeAdLoadFail(AdError adError) {
        // 广告加载失败，可通过AdError.getFullErrorInfo()获取全部错误信息
        DebugTool.i(TAG, "onNativeAdLoadFail:" + adError.getFullErrorInfo());
    }


    @Override
    public void onAdImpressed(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo) {
        // 广告展示回调
    }

    @Override
    public void onAdClicked(ATNativeAdView atNativeAdView, ATAdInfo atAdInfo) {
        // 广告点击回调
    }

    @Override
    public void onAdVideoStart(ATNativeAdView atNativeAdView) {
        // 广告视频播放开始
    }

    @Override
    public void onAdVideoEnd(ATNativeAdView atNativeAdView) {
        // 广告视频播放结束
    }

    @Override
    public void onAdVideoProgress(ATNativeAdView atNativeAdView, int i) {
        // 广告视频播放进度
    }
}
