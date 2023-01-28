package com.csrcb.design.order.pojo;

/**
 * @ClassName OrderStateChangeAction
 * @Description 订单的操作的枚举
 * @Author gangye
 * @Date 2022/12/18
 */
public enum OrderStateChangeAction {
    PAY_ORDER,// 支付操作
    SEND_ORDER,// 发货操作
    RECEIVE_ORDER;// 收货操作
}
