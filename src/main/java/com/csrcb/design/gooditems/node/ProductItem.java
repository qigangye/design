package com.csrcb.design.gooditems.node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductItem extends AbstractProductItem{
    private String id;
    private String pid;
    private String name;
    private List<ProductItem> childs = new ArrayList<>();

    public ProductItem() {
    }

    public ProductItem(String id, String pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    @Override
    public void removeChild(AbstractProductItem item) {
        ProductItem removeItem = (ProductItem) item;
        this.childs = childs.stream().filter(x -> !x.getId().equals(removeItem.getId())).collect(Collectors.toList());
    }

    @Override
    public void addChild(AbstractProductItem item) {
        this.childs.add((ProductItem) item);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductItem> getChilds() {
        return childs;
    }

    public void setChilds(List<ProductItem> childs) {
        this.childs = childs;
    }
}
