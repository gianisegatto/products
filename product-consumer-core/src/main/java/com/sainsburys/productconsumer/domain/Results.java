package com.sainsburys.productconsumer.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the result of the products found and total sum of all products
 * inside the products list.
 */
public class Results {

    private List<Product> products = new ArrayList<>();

    private Double total;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(final List<Product> products) {
        this.products = products;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
