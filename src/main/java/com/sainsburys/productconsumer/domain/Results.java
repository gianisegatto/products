package com.sainsburys.productconsumer.domain;

import org.springframework.util.CollectionUtils;

import java.text.NumberFormat;
import java.util.List;

public class Results {

    private List<Product> products;

    private Double total;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
