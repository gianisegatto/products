package com.sainsburys.productconsumer.service;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a way to read all products link from the Sainsbury' product list page.
 * The products list page URL is configured in the productsPageUrl attribute in the  application.properties file.
 */
@Service
public class ProductListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductListService.class);
    private static final String PRODUCTS_LINK_ELEMENT = ".productLister a[href]";
    private static final String LINK_ATTRIBUTE = "abs:href";

    @Value("${products.page.url}")
    private String productsPageUrl;

    @Value("${connection.tiemout}")
    private int connectionTimeout;

    /**
     * Provides a list of product link base on the Sainsbury's product list page.
     * @return List of links
     */
    public List<String> process() {

        List<String> productLines = new ArrayList<>();

        try {

            productLines = Jsoup.connect(productsPageUrl).timeout(connectionTimeout).get()
                    .select(PRODUCTS_LINK_ELEMENT)
                    .stream()
                    .map(element -> element.attr(LINK_ATTRIBUTE))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            LOGGER.error("Error during list products links", e);
        }

        return productLines;
    }
}
