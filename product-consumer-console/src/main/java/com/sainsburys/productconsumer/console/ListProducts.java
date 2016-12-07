package com.sainsburys.productconsumer.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.service.ProductDetailsService;
import com.sainsburys.productconsumer.service.ProductListService;
import com.sainsburys.productconsumer.service.ProductService;

import java.util.Optional;

public class ListProducts {

    private static final String PRODUCTS_LIST_URL = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";

    public static void main(String[] args) {

        ProductListService productListService = new ProductListService(PRODUCTS_LIST_URL, 2000);

        ProductDetailsService productDetailsService = new ProductDetailsService(2000);

        ProductService productService = new ProductService(productListService, productDetailsService);

        Optional<Results> results = productService.listProducts();

        ObjectMapper objectMapper = new ObjectMapper();
        String resultsJson = null;
        try {
            resultsJson = objectMapper.writeValueAsString(results.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(resultsJson);
    }
}
