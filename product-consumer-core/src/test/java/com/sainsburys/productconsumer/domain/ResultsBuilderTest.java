package com.sainsburys.productconsumer.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ResultsBuilderTest {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String SIZE = "1kb";

    @Test
    public void should_BuildResults() {

        Product product = new Product();
        product.setTitle(TITLE);
        product.setDescription(DESCRIPTION);
        product.setSize(SIZE);
        product.setUnitPrice(10f);

        Results results = new ResultsBuilder().setProducts(Arrays.asList(product, product)).build();

        assertThat(results, is(notNullValue()));
        assertThat(results.getProducts().size(), is(2));
        assertThat(results.getProducts().get(0).getTitle(), is(TITLE));
        assertThat(results.getProducts().get(0).getDescription(), is(DESCRIPTION));
        assertThat(results.getProducts().get(0).getSize(), is(SIZE));
        assertThat(results.getTotal(), is(20.0));
    }

    @Test
    public void should_BuildNullProductsZeroTotal() {

        Results results = new ResultsBuilder().build();

        assertThat(results, is(notNullValue()));
        assertThat(results.getProducts().size(), is(0));
        assertThat(results.getTotal(), is(0.0));
    }

}