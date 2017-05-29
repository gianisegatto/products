package com.sainsburys.productconsumer.service;


import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.domain.ResultsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
/**
 * This class provides a list of products and the sum of the all prices executing in a non-blocking way.
 */
@Service
public class ProductServiceAsync {

    private ExecutorService executor;

    private ProductListService productListService;

    private ProductDetailsService productDetailsService;

    @Autowired
    public ProductServiceAsync(ExecutorService executorService,
                               ProductListService productListService,
                               ProductDetailsService productDetailsService) {

        this.executor = executorService;
        this.productListService = productListService;
        this.productDetailsService = productDetailsService;
    }

    /**
     * Provides a list of products based on the Sainsbury's products page executing in parallel
     * all calls to the Sainsbury's pages.
     * @return Results of products.
     */
    public CompletableFuture<Results> listProducts() {

        CompletableFuture<List<String>> productsList = CompletableFuture.supplyAsync(() -> productListService.process());

        return productsList
                .thenApply(this::processLinks)
                .thenCompose(this::joinProducts)
                .thenApply(products -> new ResultsBuilder().setProducts(products).build());
    }

    /**
     * Call the product detail service sending the link of the product detail page executing
     * in parallel the calls.
     * @param productLines List of the products from the Sainsbury' products page
     * @return List of products based on the the products list
     */
    private CompletableFuture<Product>[] processLinks(final List<String> productLines) {
        return productLines.stream()
                .map(link -> supplyAsync(() -> productDetailsService.process(link), executor))
                .toArray(CompletableFuture[]::new);
    }

    /**
     * Join all the result calls in a unique list of products filtering null products.
     * @param products Array of products executed in parallel
     * @return List of products executed in parallel
     */
    private CompletableFuture<List<Product>> joinProducts(final CompletableFuture<Product>[] products) {
        return allOf(products).thenApply(
                v -> Stream.of(products)
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }
}
