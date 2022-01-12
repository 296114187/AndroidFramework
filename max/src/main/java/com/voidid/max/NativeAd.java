package com.voidid.max;


import com.voidid.core.baseclass.BaseAD;

public class NativeAd extends BaseAD {

    /**
     * 获取NativeAd广告对象
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static NativeAd getInstance(String placementID) {
        NativeAd ad = (NativeAd) NativeAd.adMap.get(placementID);

        if (ad != null) {
            ad = new NativeAd(placementID);
            ad.setID(placementID);
            NativeAd.adMap.put(placementID, ad);
        }

        return ad;
    }

    public NativeAd(String placementID) {

    }

    @Override
    public void load() {

    }

    @Override
    public void show() {

    }

    @Override
    public void destroy() {

    }
}
