package com.csrcb.design.order.pojo;

/**
 * @ClassName OrderState
 * @Description 订单状态的枚举
 * @Author gangye
 * @Date 2022/12/18
 */
public enum OrderState {
    ORDER_WAIT_PAY,//待支付
    ORDER_WAIT_SEND,// 待发货
    ORDER_WAIT_RECEIVE,// 待收货
    ORDER_FINISH; // 订单完成
}
