package com.voidid.core.enums;

import androidx.annotation.NonNull;

/**
 * 国际货币类型
 */
public enum CurrencyType {
    /** 美国-美元 **/
    USD("USD"),
    /** 中国-人民币 **/
    RMB("CNY"),
    /** 香港-港币 **/
    HKD("HKD"),
    /** 瑞士-法郎 **/
    CHF("CHF"),
    /** 日本-日元 **/
    JPY("JPY"),
    /** 韩国-韩元 **/
    KRW("KRW"),
    /** 泰国-泰铢 **/
    THB("THB"),
    /** 俄罗斯-卢布 **/
    RUR("RUR"),
    /** 欧盟-欧元 **/
    ECU("ECU"),
    /** 英国-英镑 **/
    GBP("GBP"),
    /** 法国-法郎 **/
    FRF("FRF");
    private final String text;
    CurrencyType(final String text){
        this.text = text;
    }
    @NonNull
    @Override
    public String toString(){
        return text;
    }
}
