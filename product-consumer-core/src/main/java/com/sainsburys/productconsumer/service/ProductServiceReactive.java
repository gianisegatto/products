package com.sainsburys.productconsumer.service;


import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.domain.ResultsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.functions.Action2;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.Arrays.*;

@Service
public class ProductServiceReactive {

    private ExecutorService executor;

    private ProductListService productListService;

    private ProductDetailsService productDetailsService;

    @Autowired
    public ProductServiceReactive(ExecutorService executorService,
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
    public Observable<Results> listProducts() {

        ResultsBuilder resultsBuilder = new ResultsBuilder();

        return Observable.from(productListService.process())
                .map(productDetailsService::process)
                .onErrorReturn(null)
                .filter(product -> product != null)
                .subscribeOn(Schedulers.from(executor))
                .toList()
                .map(products -> resultsBuilder.setProducts(products))
                .subscribeOn(Schedulers.from(executor))
                .map(ResultsBuilder::build)
                .subscribeOn(Schedulers.from(executor));
    }

}
