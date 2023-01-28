package com.csrcb.design.order;

import com.csrcb.design.order.pojo.Order;
import com.csrcb.design.order.pojo.OrderState;
import com.csrcb.design.order.pojo.OrderStateChangeAction;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderStateListener
 * @Description 观察者/监听
 * @Author gangye
 * @Date 2023/1/10
 */
// 监听器是监听到action后进行状态的变更
@Component("orderStateListener")
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {
    @OnTransition(source = "ORDER_WAIT_PAY", target = "ORDER_WAIT_SEND")
    public boolean payToSend(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderState(OrderState.ORDER_WAIT_SEND);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_SEND", target = "ORDER_WAIT_RECEIVE")
    public boolean sendToReceive(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderState(OrderState.ORDER_WAIT_RECEIVE);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_RECEIVE", target = "ORDER_FINISH")
    public boolean receiveToFinish(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderState(OrderState.ORDER_FINISH);
        return true;
    }
}
