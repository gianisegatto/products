package com.sainsburys.productconsumer.service;


import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.domain.ResultsBuilder;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
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

    private static final String LINK_ATTRIBUTE = "abs:href";

    @Value("${products.page.url}")
    private String productsPageUrl;

    @Value("${jsoup.tiemout}")
    private int jSoupTimeout;

    @Autowired
    private ExecutorService executor;

    @Autowired
    private ProductListPageService productListPageService;

    @Autowired
    private ProductDetailPageService productDetailPageService;

    /**
     * Provides a list of products based on the Sainsbury's products page executing in parallel
     * all calls to the Sainsbury's pages.
     * @return List of products from the Sainsbury' products page
     */
    public DeferredResult<ResponseEntity<Results>> listProducts() {

        DeferredResult<ResponseEntity<Results>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> this.executeAsync(), executor)
                .whenCompleteAsync((response, e) -> {
                    response.exceptionally(ex -> {
                        deferredResult.setErrorResult(ex.getCause());
                        return null;
                    });
                    response.thenAccept(results -> {
                        deferredResult.setResult(new ResponseEntity<>(results, HttpStatus.OK));
                    });
                });

        return deferredResult;
    }

    /**
     * Executes all calls to the Sainbury's pages async.
     * @return Results of products.
     */
    private CompletableFuture<Results> executeAsync() {

        CompletableFuture<List<Element>> productsList = CompletableFuture.supplyAsync(() -> productListPageService.list());

        CompletableFuture<Results> completableFuture = productsList
                .thenApply(this::processLinks)
                .thenCompose(this::joinProducts)
                .thenApply(products -> new ResultsBuilder().products(products).build());

        return completableFuture;
    }

    /**
     * Call the product detail service sending the link of the product detail page executing
     * in parallel the calls.
     * @param productLines List of the products from the Sainsbury' products page
     * @return List of products based on the the products list
     */
    private CompletableFuture<Product>[] processLinks(final List<Element> productLines) {
        return productLines.stream()
                .map(element -> element.attr(LINK_ATTRIBUTE))
                .map(link -> supplyAsync(() -> productDetailPageService.process(link), executor))
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
                        .filter(value -> value != null)
                        .collect(Collectors.toList()));
    }
}
