package com.csrcb.design.gooditems.vistor;

import com.csrcb.design.MockDb;
import com.csrcb.design.gooditems.node.ProductItem;
import org.springframework.stereotype.Component;

@Component
public class AddItemVistor implements ItemVistor<ProductItem> {
    // 入参是 id=2，pid=1
    @Override
    public ProductItem vistor(ProductItem productItem) {
        ProductItem currentItem = MockDb.PRODUCT_ITEM;// 从缓存来的
        if (productItem.getId().equals(currentItem.getId())) {
            throw new UnsupportedOperationException("根节点是唯一的");
        }
        if (productItem.getPid().equals(currentItem.getId())) {
            currentItem.addChild(productItem);
            return currentItem;
        }
        addChild (productItem, currentItem);
        return currentItem;
    }

    private void addChild(ProductItem productItem, ProductItem currentItem) {
        for (ProductItem item : currentItem.getChilds()) {
            if (item.getId().equals(productItem.getPid())) {
                item.addChild(productItem);
                break;
            }else {
                addChild(productItem, item);
            }
        }
    }
}
