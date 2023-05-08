package com.csrcb.design.controller;

import com.csrcb.design.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsController {
    @Autowired
    private EsService esService;

    @PostMapping("es")
    public Boolean esQuery (@RequestParam String query, Long fetchSize){
        return esService.query(query, fetchSize);
    }
}
