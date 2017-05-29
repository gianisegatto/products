package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.domain.Product;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

public class ProductDetailsServiceTest {

    private static final int CONNECTION_TIMEOUT = 1000;

    private ProductDetailsService productDetailsService = new ProductDetailsService(CONNECTION_TIMEOUT);

    @Test
    public void should_ReturnApricot() {
        Product product = productDetailsService.process("http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/sainsburys-apricot-ripe---ready-320g.html");
        assertThat(product, is(notNullValue()));
        assertThat(product.getTitle(), is("Sainsbury's Apricot Ripe & Ready x5"));
        assertThat(product.getDescription(), is("Apricots"));
    }

    @Test
    public void should_ReturnNull_NotExistUrl() {
        Product product = productDetailsService.process("http://lkjdfoioi");
        assertThat(product, is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_NullUrl() {
        productDetailsService.process(null);
    }
}