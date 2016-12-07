package com.sainsburys.productconsumer.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainsburys.productconsumer.configuration.ExecutorServiceConfiguration;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.service.ProductDetailsService;
import com.sainsburys.productconsumer.service.ProductListService;
import com.sainsburys.productconsumer.service.ProductServiceAsync;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ListProductsAsync {

    private static final String PRODUCTS_LIST_URL = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";

    public static void main(String[] args) {

        ExecutorServiceConfiguration executorServiceConfiguration = new ExecutorServiceConfiguration(20, 40, 1000);
        ExecutorService executorService = executorServiceConfiguration.executorService();

        ProductListService productListService = new ProductListService(PRODUCTS_LIST_URL, 2000);

        ProductDetailsService productDetailsService = new ProductDetailsService(2000);

        ProductServiceAsync productServiceAsync = new ProductServiceAsync(executorService, productListService, productDetailsService);

        try {
            Results results = productServiceAsync.listProducts().get();
            ObjectMapper objectMapper = new ObjectMapper();
            String resultsJson = null;
            try {
                resultsJson = objectMapper.writeValueAsString(results);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            System.out.println(resultsJson);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
