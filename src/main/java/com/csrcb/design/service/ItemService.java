package com.csrcb.design.service;

import com.csrcb.design.MockDb;
import com.csrcb.design.gooditems.node.ProductItem;
import com.csrcb.design.gooditems.vistor.AddItemVistor;
import com.csrcb.design.gooditems.vistor.DelItemVistor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    private DelItemVistor delItemVistor;
    @Autowired
    private AddItemVistor addItemVistor;

    // 这部分只有初始化的时候获取一次 或者 直接预热到缓存中
    public ProductItem getItem (){
        System.out.println("从DB中获取所有的目录");
        System.out.println("将数据组装为 ProductItem");
        System.out.println("将组装好的 ProductItem 放入缓存中，永不过期");
        return MockDb.PRODUCT_ITEM;
    }

    public ProductItem delItem (ProductItem productItem){
        ProductItem item = delItemVistor.vistor(productItem);
        MockDb.PRODUCT_ITEM = item; //更新缓存
        System.out.println("更新DB");
        return item;
    }

    public ProductItem addItem (ProductItem productItem){
        ProductItem item = addItemVistor.vistor(productItem);
        MockDb.PRODUCT_ITEM = item; //更新缓存
        System.out.println("更新DB");
        return item;
    }
}
