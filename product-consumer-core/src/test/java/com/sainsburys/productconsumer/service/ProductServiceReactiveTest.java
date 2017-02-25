package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.configuration.ExecutorServiceConfiguration;
import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import rx.Observable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceReactiveTest {

    private ExecutorService executor;

    @Mock
    private Product product;

    @Mock
    private ProductListService productListService;

    @Mock
    private ProductDetailsService productDetailsService;

    @InjectMocks
    private ProductServiceReactive productServiceReactive;

    @Before
    public void setUp() {

        ExecutorServiceConfiguration executorServiceConfiguration = new ExecutorServiceConfiguration();
        ReflectionTestUtils.setField(executorServiceConfiguration, "corePoolSize", 20);
        ReflectionTestUtils.setField(executorServiceConfiguration, "maximumPoolSize", 40);
        ReflectionTestUtils.setField(executorServiceConfiguration, "keepAliveTime", 1000);

        executor = executorServiceConfiguration.executorService();

        ReflectionTestUtils.setField(productServiceReactive, "executor", executor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException() {

        when(productListService.process()).thenThrow(new IllegalArgumentException());

        productServiceReactive.listProducts();
    }

    @Test
    public void should_ReturnProducts() throws IOException {

        when(productListService.process()).thenReturn(Arrays.asList("", "", "", ""));
        when(productDetailsService.process(anyString())).thenReturn(product);

        Observable<Results> resultsObservable = productServiceReactive.listProducts();

        Results results = resultsObservable.toBlocking().first();

        verify(productListService, times(1)).process();
        verify(productDetailsService, times(4)).process(anyString());

        assertNotNull(resultsObservable);
        assertNotNull(results);
        assertNotNull(results.getProducts());
        assertThat(results.getProducts().size(), is(4));
    }

}