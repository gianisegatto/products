package com.sainsburys.productconsumer.service;

import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ProductListPageServiceTest {

    private ProductListPageService productListPageService = new ProductListPageService();

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_UrlNull() {
        productListPageService.list();
    }

    @Test
    public void should_EmptyList_InvalidUrl() {
        ReflectionTestUtils.setField(productListPageService, "productsPageUrl", "http://dlkdlkj");
        List<Element> elements = productListPageService.list();
        assertThat(elements.size(), is(0));
    }

    @Test
    public void should_ReturnProductLines() {
        ReflectionTestUtils.setField(productListPageService, "productsPageUrl", "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html");
        List<Element> productLines = productListPageService.list();
        assertNotNull(productLines);
        assertThat(productLines.size(), is(7));
    }
}