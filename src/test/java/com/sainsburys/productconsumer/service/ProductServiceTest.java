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
    private Elements elements;

    @Mock
    private ProductListPageService productListPageService;

    @Mock
    private ProductDetailPageService productDetailPageService;

    @InjectMocks
    private ProductService productService;

    @Test
    public void should_ReturnProducts() throws IOException {
        when(productListPageService.list()).thenReturn(elements);
        when(elements.stream()).thenReturn(Arrays.asList(element).stream());
        when(element.attr("abs:href")).thenReturn("http://localhost:9080");
        when(productDetailPageService.process("http://localhost:9080")).thenReturn(product);

        Optional<Results> results = productService.listProducts();

        verify(productListPageService, times(1)).list();
        verify(element, times(1)).attr("abs:href");
        verify(productDetailPageService, times(1)).process("http://localhost:9080");

        assertNotNull(results.get());
        assertThat(results.get().getProducts().size(), is(1));
        assertThat(results.get().getTotal(), is(0.0));
    }
}