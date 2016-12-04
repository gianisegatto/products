package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.domain.ResultsBuilder;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final String LINK_ATTRIBUTE = "abs:href";

    @Value("${products.page.url}")
    private String productsPageUrl;

    @Value("${jsoup.tiemout}")
    private int jSoupTimeout;

    @Autowired
    private ProductListPageService productListPageService;

    @Autowired
    private ProductDetailPageService productDetailPageService;

    public Optional<Results> listProducts() {

        Results results = null;

        List<Product> products = processProducts(productListPageService.list());

        results = new ResultsBuilder().products(products).build();

        return Optional.ofNullable(results);
    }

    private List<Product> processProducts(List<Element> productLines) {
        return productLines.stream()
                        .map(line -> line.attr(LINK_ATTRIBUTE))
                        .map(productDetailPageService::process)
                        .collect(Collectors.toList());
    }
}