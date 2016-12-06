package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.configuration.ExecutorServiceConfiguration;
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
    private ProductListService productListService;

    @Mock
    private ProductDetailsService productDetailsService;

    @InjectMocks
    private ProductServiceAsync productServiceAsync;

    @Before
    public void setUp() {

        ExecutorServiceConfiguration executorServiceConfiguration = new ExecutorServiceConfiguration();
        ReflectionTestUtils.setField(executorServiceConfiguration, "corePoolSize", 20);
        ReflectionTestUtils.setField(executorServiceConfiguration, "maximumPoolSize", 40);
        ReflectionTestUtils.setField(executorServiceConfiguration, "keepAliveTime", 1000);

        executor = executorServiceConfiguration.executorService();

        ReflectionTestUtils.setField(productServiceAsync, "executor", executor);
    }

    @Test
    public void should_ThrowIllegalArgumentException() {

        when(productListService.process()).thenThrow(new IllegalArgumentException());

        productServiceAsync.listProducts();

        verify(productListService, times(0)).process();
        verify(productDetailsService, times(0)).process(anyString());
    }

    @Test
    public void should_ReturnProducts() throws IOException {

        when(productListService.process()).thenReturn(Arrays.asList("", ""));
        when(elements.stream()).thenReturn(Arrays.asList(element, element).stream());
        when(productDetailsService.process(anyString())).thenReturn(product);

        productServiceAsync.listProducts();

        verify(productListService, times(1)).process();
        verify(productDetailsService, times(2)).process(anyString());
    }
}