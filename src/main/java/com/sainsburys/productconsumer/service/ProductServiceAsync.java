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

@Service
public class ProductServiceAsync {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceAsync.class);
    private static final String PRODUCTS_LINK_ELEMENT = ".productLister a[href]";
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

    private CompletableFuture<Results> executeAsync() {

        CompletableFuture<List<Element>> productsList = CompletableFuture.supplyAsync(() -> productListPageService.list());

        CompletableFuture<Results> completableFuture = productsList
                .thenApply(this::processLinks)
                .thenCompose(this::joinProducts)
                .thenApply(products -> new ResultsBuilder().products(products).build());

        return completableFuture;
    }

    private CompletableFuture<Product>[] processLinks(List<Element> elements) {
        return elements.stream()
                .map(element -> element.attr(LINK_ATTRIBUTE))
                .map(link -> supplyAsync(() -> productDetailPageService.process(link), executor))
                .toArray(CompletableFuture[]::new);
    }

    private CompletableFuture<List<Product>> joinProducts(final CompletableFuture<Product>[] futures) {
        return allOf(futures).thenApply(
                v -> Stream.of(futures)
                        .map(CompletableFuture::join)
                        .filter(value -> value != null)
                        .collect(Collectors.toList()));
    }
}
