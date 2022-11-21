package com.csrcb.design.controller;

import com.csrcb.design.pay.pojo.PayBody;
import com.csrcb.design.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname PayController
 * @Date 2022/9/25 21:16
 * @Created by gangye
 */
@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping("/pay")
    public Boolean pay(@RequestBody PayBody payBody){
        return payService.pay(payBody);
    }
}
