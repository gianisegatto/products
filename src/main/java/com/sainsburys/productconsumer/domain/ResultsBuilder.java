package com.sainsburys.productconsumer.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class ResultsBuilder {

    private static final int HUNDRED_ROUND = 100;

    private List<Product> products;

    public ResultsBuilder products(final List<Product> products) {
        this.products = products;
        return this;
    }

    public Results build() {
        Results results;
        results = new Results();
        results.setProducts(products);
        results.setTotal(sumProductsPrice(products));
        return results;
    }

    private Double sumProductsPrice(final List<Product> products) {
        double total = 0;
        if (!CollectionUtils.isEmpty(products)) {
            total = products.stream().mapToDouble(product -> product.getUnitPrice()).sum();
        }
        return (double) Math.round(total * HUNDRED_ROUND) / HUNDRED_ROUND;
    }
}
