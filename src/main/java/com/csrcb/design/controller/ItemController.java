package com.csrcb.design.controller;

import com.csrcb.design.gooditems.node.ProductItem;
import com.csrcb.design.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("get")
    public ProductItem getItem(){
        return itemService.getItem();
    }

    @PostMapping("del")
    public ProductItem delItem(@RequestBody ProductItem productItem){
        return itemService.delItem(productItem);
    }

    @PostMapping("add")
    public ProductItem addItem(@RequestBody ProductItem productItem){
        return itemService.addItem(productItem);
    }
}
