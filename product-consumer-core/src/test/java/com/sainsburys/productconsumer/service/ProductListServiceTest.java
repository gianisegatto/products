package com.sainsburys.productconsumer.service;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ProductListServiceTest {

    private ProductListService productListService = new ProductListService();

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_UrlNull() {
        productListService.process();
    }

    @Test
    public void should_EmptyList_InvalidUrl() {
        ReflectionTestUtils.setField(productListService, "productsPageUrl", "http://dlkdlkj");
        List<String> elements = productListService.process();
        assertThat(elements.size(), is(0));
    }

    @Test
    public void should_ReturnProductLines() {
        ReflectionTestUtils.setField(productListService, "productsPageUrl", "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html");
        List<String> productLines = productListService.process();
        assertNotNull(productLines);
        assertThat(productLines.size(), is(7));
    }
}