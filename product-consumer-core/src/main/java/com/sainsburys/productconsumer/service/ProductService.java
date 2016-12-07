package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.domain.ResultsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class provides a list of products and the sum of the all prices executing in a sequence way.
 */
@Service
public class ProductService {

    @Autowired
    private ProductListService productListService;

    @Autowired
    private ProductDetailsService productDetailsService;

    public ProductService() {
    }

    public ProductService(ProductListService productListService, ProductDetailsService productDetailsService) {
        this.productListService = productListService;
        this.productDetailsService = productDetailsService;
    }

    /**
     * Provides a list of products based on the Sainsbury's products page executing in sequence
     * each link from the page.
     * @return List of products from the Sainsbury' products page
     */
    public Optional<Results> listProducts() {

        Results results = null;

        List<Product> products = processProducts(productListService.process());

        results = new ResultsBuilder().setProducts(products).build();

        return Optional.ofNullable(results);
    }

    /**
     * Call the product detail service sending the link of the product detail page executing on the
     * sequence of the productLines list.
     * @param productLines List of the products from the Sainsbury' products page
     * @return List of products based on the the products list
     */
    private List<Product> processProducts(final List<String> productLines) {
        return productLines.stream()
                        .map(productDetailsService::process)
                        .filter(product -> product != null)
                        .collect(Collectors.toList());
    }
}