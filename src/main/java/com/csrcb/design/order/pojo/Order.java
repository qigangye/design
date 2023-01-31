package com.csrcb.design.order.pojo;

/**
 * @ClassName Order
 * @Description Order的pojo类
 * @Author gangye
 * @Date 2022/12/18
 */
// 状态转化的一个控制机。状态机：初始化状态；配置我们所有状态之间的转化关系；一些持久化的操作（如：redis）
public class Order {
    private Integer orderId;
    // 订单状态
    private OrderState orderState;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
