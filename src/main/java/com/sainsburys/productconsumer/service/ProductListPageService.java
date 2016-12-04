package com.sainsburys.productconsumer.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductListPageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductListPageService.class);
    private static final String PRODUCTS_LINK_ELEMENT = ".productLister a[href]";

    @Value("${products.page.url}")
    private String productsPageUrl;

    @Value("${jsoup.tiemout}")
    private int jSoupTimeout;

    public List<Element> list() {

        Elements productLines = new Elements();

        try {

            productLines = Jsoup.connect(productsPageUrl).timeout(jSoupTimeout).get().select(PRODUCTS_LINK_ELEMENT);

        } catch (IOException e) {
            LOGGER.error("Error during list products links", e);
        }

        return productLines;
    }
}