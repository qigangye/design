package com.csrcb.design;

import com.csrcb.design.gooditems.node.ProductItem;

import java.util.ArrayList;
import java.util.List;

// 模拟数据库的预热数据mock
public class MockDb {
    public static ProductItem PRODUCT_ITEM = new ProductItem();
    static {
        PRODUCT_ITEM.setId("1");
        PRODUCT_ITEM.setPid("0");
        PRODUCT_ITEM.setName("书籍");
        List<ProductItem> childs = new ArrayList<>();
        ProductItem c1 = new ProductItem("2","1", "技术书籍");
        ProductItem c2 = new ProductItem("3","1", "历史书籍");
        List<ProductItem> childs1 = new ArrayList<>();
        ProductItem c3 = new ProductItem("4","2", "并发编程");
        ProductItem c4 = new ProductItem("5","2", "JVM");
        childs1.add(c3);
        childs1.add(c4);
        c1.setChilds(childs1);
        childs.add(c1);
        childs.add(c2);
        PRODUCT_ITEM.setChilds(childs);
    }
}
