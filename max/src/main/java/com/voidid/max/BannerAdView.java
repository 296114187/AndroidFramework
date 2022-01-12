package com.voidid.max;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.voidid.core.AppGlobal;
import com.voidid.core.baseclass.BaseAD;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.tools.DebugTool;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BannerAdView extends BaseAD implements MaxAdViewAdListener, MaxAdRevenueListener {
    private static final String TAG = BannerAdView.class.getName();
    private boolean isLoading = false;

    /**
     * 获取RewardVideoAd广告对象
     *
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static BannerAdView getInstance(String placementID, JSONObject style) {
        BannerAdView ad = (BannerAdView) BannerAdView.adMap.get(placementID);
        if (ad == null) {
            ad = new BannerAdView(placementID);
            ad.setID(placementID);
            BannerAdView.adMap.put(placementID, ad);
        }

        return ad;
    }

    /**
     * 创建广告
     */
    public BannerAdView(String placementID) {
        DebugTool.d(TAG, "create()");
        this.ad = new MaxAdView(placementID, AppGlobal.getMainActivity());
        ((MaxAdView)this.ad).setListener(this);
//        ((MaxAdView)ad).setRevenueListener(this);
        // 拉伸到屏幕的宽度以使横幅完全发挥作用
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        // 手机和平板电脑的横幅高度分别为 50 和 90
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(AppGlobal.getMainActivity()).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx(AppGlobal.getContext(), heightDp);
        // 设置AD 布局宽度高度和位置
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, heightPx);
        layoutParams.gravity = Gravity.BOTTOM;              // 设置 com.ily.framework.AD View 位置（底部）
        // 设置布局宽高
        ((MaxAdView)this.ad).setLayoutParams(layoutParams);
        // 自适应Banner
        ((MaxAdView)this.ad).setExtraParameter("adaptive_banner", "true");
        // 为横幅设置背景或背景颜色以使其功能齐全
//        ((MaxAdView)this.ad).setBackgroundColor(Color.BLACK);
        // 获取根布局容器
        ViewGroup rootView = AppGlobal.getMainActivity().findViewById(android.R.id.content);
        // 将Banner布局添加到根布局上
        AppGlobal.getMainActivity().runOnUiThread(() -> rootView.addView(((MaxAdView)this.ad)));
        load();
        hide();
    }

    /**
     * 加载广告
     */
    @Override
    public void load() {
        if (this.isLoading) {
            DebugTool.d(TAG, "插屏视频正在加载，请勿重复加载...");
            return;
        }
        this.isLoading = true;
        ((MaxAdView)this.ad).loadAd();
    }

    /**
     * 显示广告
     */
    @Override
    public void show() {
        if (this.isReady()) {
            ((MaxAdView)this.ad).setVisibility(View.VISIBLE);
            ((MaxAdView)this.ad).startAutoRefresh();
            this.isLoadToShow = false;
        } else {
            // 广告未就绪，重新触发加载
            this.load();
            this.isLoadToShow = true;
        }
    }

    /**
     * 移除广告
     */
    public void hide() {
        ((MaxAdView)this.ad).setVisibility(View.GONE);
        ((MaxAdView)this.ad).stopAutoRefresh();
    }

    @Override
    public void destroy() {
        DebugTool.d(TAG, "destroy()");
    }

    /**
     * 广告是否准备好
     */
    public boolean isReady() {
        return ((MaxInterstitialAd)ad).isReady();
    }

    @Override
    public void onAdLoaded(final MaxAd adInfo) {
        DebugTool.i(TAG, "Banner加载成功");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad loaded.");
        sendEvent(ADType.BANNER, ADEventType.LOADED, map);
        this.isLoading = false;

        if (this.isLoadToShow) {
            this.show();
        }
    }

    @Override
    public void onAdLoadFailed(final String ad_id, final MaxError error) {
        DebugTool.e(TAG, "Banner加载失败:" + error.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", error.getMessage());
        map.put("errCode", error.getCode());
        sendEvent(ADType.BANNER, ADEventType.LOAD_FAILED, map);
        this.isLoading = false;
    }

    @Override
    public void onAdDisplayFailed(final MaxAd adInfo, final MaxError error) {
        DebugTool.e(TAG, "Banner展示失败:" + error.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", error.getMessage());
        map.put("errCode", error.getCode());
        sendEvent(ADType.BANNER, ADEventType.SHOW_FAILED, map);
        this.isLoading = false;
        this.load();
    }

    @Override
    public void onAdClicked(final MaxAd adInfo) {
        DebugTool.d(TAG, "Banner被点击");

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad clicked");
        sendEvent(ADType.REWARDED, ADEventType.CLICKED, map);
    }

    @Override
    public void onAdExpanded(final MaxAd adInfo) {
        DebugTool.i(TAG, "Banner展示");
    }

    @Override
    public void onAdCollapsed(final MaxAd adInfo) {
        DebugTool.i(TAG, "Banner关闭");
    }

    @Override
    public void onAdDisplayed(final MaxAd adInfo) {
        DebugTool.i(TAG, "Banner展示结束");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed");
        sendEvent(ADType.BANNER, ADEventType.SHOWED, map);
    }

    @Override
    public void onAdHidden(final MaxAd adInfo) {
        DebugTool.d(TAG, "Banner广告关闭");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad close.");
        sendEvent(ADType.BANNER, ADEventType.LOADED, map);
        // 加载下一次的广告
        this.load();
    }

    @Override
    public void onAdRevenuePaid(MaxAd ad) {
        DebugTool.i(TAG, "Banner收入");
    }
}
