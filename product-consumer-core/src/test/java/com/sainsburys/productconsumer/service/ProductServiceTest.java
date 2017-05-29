package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private Product product;

    @Mock
    private ProductListService productListService;

    @Mock
    private ProductDetailsService productDetailsService;

    @InjectMocks
    private ProductService productService;

    @Test
    public void should_ReturnProducts() throws IOException {

        when(productListService.process()).thenReturn(Arrays.asList("", ""));
        when(productDetailsService.process(anyString())).thenReturn(product);
        when(product.getUnitPrice()).thenReturn(10f);

        Optional<Results> results = productService.listProducts();

        verify(productListService, times(1)).process();
        verify(productDetailsService, times(2)).process(anyString());

        assertThat(results, is(notNullValue()));
        assertThat(results.get().getProducts().size(), is(2));
        assertThat(results.get().getTotal(), is(20.0));
    }

    @Test
    public void should_NotReturnProducts() throws IOException {

        when(productListService.process()).thenReturn(Arrays.asList(""));
        when(productDetailsService.process(anyString())).thenReturn(null);

        Optional<Results> results = productService.listProducts();

        verify(productListService, times(1)).process();
        verify(productDetailsService, times(1)).process(anyString());

        assertThat(results, is(notNullValue()));
        assertThat(results.get().getProducts().size(), is(0));
        assertThat(results.get().getTotal(), is(0.0));
    }

    @Test
    public void should_NotReturnProducts_ProductListEmpty() throws IOException {
        when(productListService.process()).thenReturn(Collections.emptyList());

        Optional<Results> results = productService.listProducts();

        verify(productListService, times(1)).process();
        verify(productDetailsService, times(0)).process(anyString());

        assertThat(results, is(notNullValue()));
        assertThat(results.get().getProducts().size(), is(0));
        assertThat(results.get().getTotal(), is(0.0));
    }
}