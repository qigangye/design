package com.csrcb.design.service;

import com.csrcb.design.order.pojo.Order;
import com.csrcb.design.order.pojo.OrderState;
import com.csrcb.design.order.pojo.OrderStateChangeAction;
import com.csrcb.design.pay.facade.StrategyFacade;
import com.csrcb.design.pay.pojo.PayBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PayService
 * @Date 2022/9/25 21:18
 * @Created by gangye
 */
@Service
public class OrderService {
    @Autowired
    private StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    @Autowired
    private StateMachinePersister<OrderState, OrderStateChangeAction, Order> stateMachinePersister;

    // 模拟一个存储（示例，不做连接数据库展示）
    private List<Order> orders = new ArrayList<>();
    public Order createOrder(Integer oid) {
        Order order = new Order();
        order.setOrderState(OrderState.ORDER_WAIT_PAY);
        order.setOrderId(oid);
        // 创建的order持久化至数据库中，防止下次访问的时候查询不到
        orders.add(order);//模拟存储到DB
        return order;
    }

    // 后续对于付款模块的删除或增加或修改，无需改动service
    // 不会对调用层产生任何代码的改动
    // 调用层使用pay模块，无需关系实现的逻辑，只需要将入参传递给pay模块即可
    public Order pay(PayBody payBody){
        // 书写付款逻辑
        boolean flag = false;
        flag = StrategyFacade.pay(payBody);
        if (flag) {
            Order order = orders.get(0);// 模拟从DB获取的数据
            Message message = MessageBuilder.withPayload(OrderStateChangeAction.PAY_ORDER).setHeader("order", order).build();
            // 发送消息
            if (changeStateAction(message, order)){
                return order;
            }
            // 如果是true，保存到db
            saveToDb(payBody);
        }
        return null;
    }

    private void saveToDb(PayBody payBody) {
    }

    public Order send(Integer oid) {
        Order order = orders.get(0);// 模拟从DB获取的数据
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.SEND_ORDER).setHeader("order", order).build();
        if (changeStateAction(message, order)){
            return order;
        }
        return null;
    }

    public Order receive(Integer oid) {
        Order order = orders.get(0);// 模拟从DB获取的数据
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.RECEIVE_ORDER).setHeader("order", order).build();
        if (changeStateAction(message, order)){
            return order;
        }
        return null;
    }

    /**
     * 发送订单状态转换事件
     * @param message
     * @param order
     * @return
     */
    private boolean changeStateAction(Message<OrderStateChangeAction> message, Order order){
        try {
            orderStateMachine.start();
            stateMachinePersister.restore(orderStateMachine, order);// 状态机状态的恢复，restore是取
            boolean res = orderStateMachine.sendEvent(message);
            stateMachinePersister.persist(orderStateMachine, order);// 状态机状态的存储，persist是存
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return false;
    }
}
