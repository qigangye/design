package com.csrcb.design.gooditems.vistor;

import com.csrcb.design.gooditems.node.ProductItem;

public interface ItemVistor<T> {
    T vistor(ProductItem productItem);
}
