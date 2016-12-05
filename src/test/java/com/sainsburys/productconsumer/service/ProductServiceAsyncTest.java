package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.configuration.ProductConfiguration;
import com.sainsburys.productconsumer.domain.Product;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceAsyncTest {

    private ExecutorService executor;

    @Mock
    private Element element;

    @Mock
    private List<Element> elements;

    @Mock
    private Product product;

    @Mock
    private ProductListPageService productListPageService;

    @Mock
    private ProductDetailPageService productDetailPageService;

    @InjectMocks
    private ProductServiceAsync productServiceAsync;

    @Before
    public void setUp() {

        ProductConfiguration productConfiguration = new ProductConfiguration();
        ReflectionTestUtils.setField(productConfiguration, "corePoolSize", 20);
        ReflectionTestUtils.setField(productConfiguration, "maximumPoolSize", 40);
        ReflectionTestUtils.setField(productConfiguration, "keepAliveTime", 1000);

        executor = productConfiguration.executorService();

        ReflectionTestUtils.setField(productServiceAsync, "executor", executor);
    }

    @Test
    public void should_ThrowIllegalArgumentException() {

        when(productListPageService.process()).thenThrow(new IllegalArgumentException());

        productServiceAsync.listProducts();

        verify(productListPageService, times(0)).process();
        verify(productDetailPageService, times(0)).process(anyString());
    }

    @Test
    public void should_ReturnProducts() throws IOException {

        when(productListPageService.process()).thenReturn(Arrays.asList(element, element));
        when(elements.stream()).thenReturn(Arrays.asList(element, element).stream());
        when(productDetailPageService.process(anyString())).thenReturn(product);

        productServiceAsync.listProducts();

        verify(productListPageService, times(1)).process();
        verify(productDetailPageService, times(2)).process(anyString());
    }
}