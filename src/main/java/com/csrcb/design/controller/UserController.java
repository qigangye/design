package com.csrcb.design.controller;

import com.csrcb.design.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserController
 * @Description 投放业务的 controller
 * @Author gangye
 * @Date 2022/11/27
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/suggest")
    public List<String> suggestBusiness(@RequestParam String username){
        return userService.suggestBusiness(username);
    }
}
