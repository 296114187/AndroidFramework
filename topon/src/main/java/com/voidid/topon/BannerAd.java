package com.voidid.topon;

import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.banner.api.ATBannerListener;
import com.voidid.core.baseclass.BaseAD;

import com.voidid.core.enums.ADType;
import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;
import com.voidid.core.enums.ADEventType;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BannerAd extends BaseAD implements ATBannerListener {
    public static String TAG = BannerAd.class.getName();

    /**
     * 获取广告对象
     *
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static BannerAd getInstance(String placementID, JSONObject style) {
        BannerAd ad = (BannerAd) BannerAd.adMap.get(placementID);

        if (ad == null) {
            ad = new BannerAd(placementID, style);
            ad.setID(placementID);
            BannerAd.adMap.put(placementID, ad);
        }

        return ad;
    }

    /**
     * 构造函数
     *
     * @param placementID 广告ID
     */
    public BannerAd(String placementID, JSONObject style) {
        DebugTool.d(TAG, "create()");
        this.ad = new ATBannerView(AppGlobal.getMainActivity());
        ATBannerView mAd = ((ATBannerView) this.ad);

        mAd.setPlacementId(placementID);
        mAd.setBannerAdListener(this);

        try {
            this.initLayout(mAd, style);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                this.initLayout(mAd, null);
            } catch (Exception jex) {
                jex.printStackTrace();
            }
        }
        this.load();
    }

    private void initLayout(ATBannerView adInstance, JSONObject style) throws Exception {
        float ratio = 320 / 50f;//必须跟TopOn后台配置的Banner广告源宽高比例一致，假设尺寸为320x50
        boolean hasLeft = false;
        try {
            style.get("left");
            hasLeft = true;
        } catch (Exception ignored) {
        }

        if (style == null || !hasLeft) {
            int orientation = AppGlobal.getApplication().getResources().getConfiguration().orientation;
            int width = AppGlobal.getContext().getResources().getDisplayMetrics().widthPixels;//定一个宽度值，比如屏幕宽度
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                width = AppGlobal.getContext().getResources().getDisplayMetrics().heightPixels;
            }
            int height = (int) (width / ratio);

            DebugTool.d(TAG, "[fl-api]Banner frameLayoutParams normal:{width:" + width + ", height:" + height + "}");
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutParams.gravity = Gravity.BOTTOM;
            } else {
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            }
            adInstance.setLayoutParams(layoutParams);
        } else {
            int width = (int) style.get("width");
            int height = (int) (width / ratio);

            DebugTool.d(TAG, "[fl-api]Banner frameLayoutParams custom:{width:" + width + ", height:" + height + "}");
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            layoutParams.leftMargin = (int) style.get("left");
            layoutParams.topMargin = (int) style.get("top");

            adInstance.setLayoutParams(layoutParams);
        }

        ViewGroup rootView = AppGlobal.getMainActivity().findViewById(android.R.id.content);
        AppGlobal.getMainActivity().runOnUiThread(() -> {
            rootView.addView(adInstance);
            adInstance.setVisibility(View.GONE);
        });
    }

    /**
     * 广告是否就绪
     *
     * @return isReady
     */
    public boolean isReady() {
        return ((ATBannerView) this.ad).checkAdStatus().isReady();
    }


    @Override
    public void load() {
        DebugTool.d(TAG, "load()");
        if (((ATBannerView) this.ad).checkAdStatus().isLoading()) {
            DebugTool.d(TAG, "[fl-api]Banner广告正在加载，请勿重复加载...");
            return;
        }
        AppGlobal.getMainActivity().runOnUiThread(() -> ((ATBannerView) ad).loadAd());
    }

    @Override
    public void show() {
        DebugTool.d(TAG, "[fl-api]show()");
        if (this.isReady()) {
            AppGlobal.getMainActivity().runOnUiThread(() -> ((ATBannerView) ad).setVisibility(View.VISIBLE));
            this.isLoadToShow = false;
        } else {
            // 广告未就绪，重新触发加载
            this.load();
            this.isLoadToShow = true;
        }
    }

    public void hide() {
        DebugTool.d(TAG, "[fl-api]hide()");
        if (!this.isReady()) {
            return;
        }
        AppGlobal.getMainActivity().runOnUiThread(() -> ((ATBannerView) ad).setVisibility(View.GONE));
    }

    @Override
    public void destroy() {
        DebugTool.d(TAG, "[fl-api]destroy()");
    }


    @Override
    public void onBannerLoaded() {
        DebugTool.d(TAG, "[fl-api]Banner广告加载成功，isLoadToShow：" + this.isLoadToShow);

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad loaded.");
        sendEvent(ADType.BANNER, ADEventType.LOADED, map);

        if (this.isLoadToShow) {
            this.show();
        }
    }

    @Override
    public void onBannerFailed(AdError adError) {
        DebugTool.e(TAG, "[fl-api]Banner广告存在错误：" + adError.getFullErrorInfo());

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getDesc());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.BANNER, ADEventType.LOAD_FAILED, map);
    }

    @Override
    public void onBannerClicked(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]Banner广告被点击");

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad clicked");
        sendEvent(ADType.BANNER, ADEventType.CLICKED, map);
    }

    @Override
    public void onBannerShow(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]Banner广告展示");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed");
        sendEvent(ADType.BANNER, ADEventType.SHOWED, map);
    }

    @Override
    public void onBannerClose(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]Banner广告关闭");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad close.");
        sendEvent(ADType.BANNER, ADEventType.LOADED, map);
        this.hide();
    }

    @Override
    public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]Banner广告自动刷新");
    }

    @Override
    public void onBannerAutoRefreshFail(AdError adError) {
        DebugTool.e(TAG, "[fl-api]Banner广告自动刷新失败:" + adError.getFullErrorInfo());
    }
}
