package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private Product product;

    @Mock
    private Element element;

    @Mock
    private List<Element> elements;

    @Mock
    private ProductListPageService productListPageService;

    @Mock
    private ProductDetailPageService productDetailPageService;

    @InjectMocks
    private ProductService productService;

    @Test
    public void should_ReturnProducts() throws IOException {

        when(productListPageService.process()).thenReturn(elements);
        when(elements.stream()).thenReturn(Arrays.asList(element, element).stream());
        when(element.attr("abs:href")).thenReturn("http://localhost:9080");
        when(productDetailPageService.process("http://localhost:9080")).thenReturn(product);
        when(product.getUnitPrice()).thenReturn(10f);

        Optional<Results> results = productService.listProducts();

        verify(productListPageService, times(1)).process();
        verify(element, times(2)).attr("abs:href");
        verify(productDetailPageService, times(2)).process("http://localhost:9080");

        assertNotNull(results.get());
        assertThat(results.get().getProducts().size(), is(2));
        assertThat(results.get().getTotal(), is(20.0));
    }

    @Test
    public void should_NotReturnProducts() throws IOException {
        when(productListPageService.process()).thenReturn(elements);
        when(elements.stream()).thenReturn(Arrays.asList(element).stream());
        when(element.attr("abs:href")).thenReturn("http://localhost:9080");
        when(productDetailPageService.process("http://notexisturl")).thenReturn(null);

        Optional<Results> results = productService.listProducts();

        verify(productListPageService, times(1)).process();
        verify(element, times(1)).attr("abs:href");
        verify(productDetailPageService, times(1)).process("http://localhost:9080");

        assertNotNull(results.get());
        assertThat(results.get().getProducts().size(), is(0));
        assertThat(results.get().getTotal(), is(0.0));
    }

    @Test
    public void should_NotReturnProducts_ProductListEmpty() throws IOException {
        when(productListPageService.process()).thenReturn(Collections.emptyList());

        Optional<Results> results = productService.listProducts();

        verify(productListPageService, times(1)).process();
        verify(element, times(0)).attr("abs:href");
        verify(productDetailPageService, times(0)).process("http://localhost:9080");

        assertNotNull(results.get());
        assertThat(results.get().getProducts().size(), is(0));
        assertThat(results.get().getTotal(), is(0.0));
    }
}