package com.csrcb.design.gooditems.vistor;

import com.csrcb.design.MockDb;
import com.csrcb.design.gooditems.node.ProductItem;
import org.springframework.stereotype.Component;

@Component
public class DelItemVistor implements ItemVistor<ProductItem> {
    // 入参是 id=2，pid=1
    @Override
    public ProductItem vistor(ProductItem productItem) {
        ProductItem currentItem = MockDb.PRODUCT_ITEM;// 从缓存来的
        if (productItem.getId().equals(currentItem.getId())) {
            throw new UnsupportedOperationException("根节点不能删除");
        }
        if (productItem.getPid().equals(currentItem.getId())) {
            currentItem.removeChild(productItem);
            return currentItem;
        }
        delChild (productItem, currentItem);
        return currentItem;
    }

    private void delChild(ProductItem productItem, ProductItem currentItem) {
        for (ProductItem item : currentItem.getChilds()) {
            if (item.getId().equals(productItem.getPid())) {
                item.removeChild(productItem);
                break;
            }else {
                delChild(productItem, item);
            }
        }
    }
}
