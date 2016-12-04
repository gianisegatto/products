package com.sainsburys.productconsumer.service;

import com.sainsburys.productconsumer.Application;
import com.sainsburys.productconsumer.configuration.ProductConfiguration;
import com.sainsburys.productconsumer.domain.Results;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = {
        Application.class})
@TestPropertySource("classpath:application.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceAsyncTest {

    @Autowired
    private ProductServiceAsync productServiceAsync;

    @Test
    public void should_ThrowIllegalArgumentException() {
        ReflectionTestUtils.setField(productServiceAsync, "productsPageUrl", null);
        DeferredResult<ResponseEntity<Results>> deferredResult = productServiceAsync.listProducts();
        deferredResult.onCompletion(() -> {
            assertNotNull(deferredResult.getResult());
            ResponseEntity<Results> responseEntity = (ResponseEntity<Results>) deferredResult.getResult();
            assertNull(responseEntity.getBody());
        });
    }

    @Test
    public void should_ReturnEmptyResult_InvalidURL() {
        ReflectionTestUtils.setField(productServiceAsync, "productsPageUrl", "http://dkjfdkl");
        DeferredResult<ResponseEntity<Results>> deferredResult = productServiceAsync.listProducts();

        deferredResult.onCompletion(() -> {
            assertNotNull(deferredResult.getResult());
            ResponseEntity<Results> responseEntity = (ResponseEntity<Results>) deferredResult.getResult();
            assertNull(responseEntity.getBody());
        });
    }

    @Test
    public void should_ReturnProducts() throws IOException {
        DeferredResult<ResponseEntity<Results>> deferredResult = productServiceAsync.listProducts();
        deferredResult.onCompletion(() -> {
            assertNotNull(deferredResult.getResult());
            ResponseEntity<Results> responseEntity = (ResponseEntity<Results>) deferredResult.getResult();
            assertNotNull(responseEntity.getBody());
            assertThat(responseEntity.getBody().getProducts().size(), is(7));
            assertThat(responseEntity.getBody().getTotal(), is(15.1));
        });
    }
}