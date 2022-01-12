package com.voidid.core.enums;

import androidx.annotation.NonNull;

public enum OrderState {
    /** 下单 **/
    PLACE_ORDER("PLACE_ORDER"),
    /** 成功支付订单 **/
    ORDER_PAY_SUCCESS("ORDER_PAY_SUCCESS"),
    /** 取消订单 **/
    CANCEL_ORDER("CANCEL_ORDER");
    private final String text;
    OrderState(final String text){
        this.text = text;
    }
    @NonNull
    @Override
    public String toString(){
        return text;
    }
}
