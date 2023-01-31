package com.csrcb.design.controller;

import com.csrcb.design.order.pojo.Order;
import com.csrcb.design.pay.pojo.PayBody;
import com.csrcb.design.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname PayController
 * @Date 2022/9/25 21:16
 * @Created by gangye
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param oid
     * @return
     */
    @GetMapping("/mkOrder")
    public Order createOrder(@RequestParam("oid") Integer oid){
        return orderService.createOrder(oid);
    }

    /**
     * 支付订单
     * @param payBody
     * @return
     */
    @PostMapping("/pay")
    public Order payOrder(@RequestBody PayBody payBody){
        return orderService.pay(payBody);
    }

    /**
     * 发送订单
     * @param oid
     * @return
     */
    @GetMapping("/send")
    public Order send(@RequestParam("oid") Integer oid){
        return orderService.send(oid);
    }

    /**
     * 确认订单收货
     * @param oid
     * @return
     */
    @GetMapping("/receive")
    public Order receive(@RequestParam("oid") Integer oid){
        return orderService.receive(oid);
    }
}
