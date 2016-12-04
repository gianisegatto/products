package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.domain.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class ProductDetailPageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailPageService.class);

    private static final int KB_SIZE = 1024;
    private static final String KB = "kb";
    private static final String TITLE_ELEMENT = "h1";
    private static final String PRICE_ELEMENT = ".pricePerUnit";
    private static final String PRODUCT_DESCRIPTION_ELEMENT = ".productText p:eq(0)";
    private static final String SLASH = "/";
    private static final int POUND_INDEX = 1;
    private static final int SQUARE = 2;

    @Value("${jsoup.tiemout}")
    private int jSoupTimeout;

    public Product process(final String link) {

        Product product = null;

        try {

            Connection.Response response = Jsoup.connect(link).timeout(jSoupTimeout).execute();
            Document productPage = response.parse();
            product = new Product();
            product.setTitle(productPage.select(TITLE_ELEMENT).text());
            product.setSize((new BigInteger(response.bodyAsBytes()).intValue() / Math.round(Math.pow(KB_SIZE, SQUARE))) + KB);
            String price = Jsoup.parse(productPage.select(PRICE_ELEMENT).text()).body().text();
            product.setUnitPrice(Float.parseFloat(price.substring(POUND_INDEX, price.indexOf(SLASH))));
            product.setDescription(productPage.select(PRODUCT_DESCRIPTION_ELEMENT).stream().findFirst().get().text());

        } catch (IOException e) {
            LOGGER.error(String.format("Error during get product page info link: %s", link), e);
        }

        return product;
    }
}
