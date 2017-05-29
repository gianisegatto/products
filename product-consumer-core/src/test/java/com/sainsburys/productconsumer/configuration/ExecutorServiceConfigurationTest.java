package com.sainsburys.productconsumer.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = {
        ExecutorServiceConfiguration.class,
        ExecutorServiceConfigurationTest.TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExecutorServiceConfigurationTest {

    @Autowired
    private ExecutorService executorService;

    // This method is just to keep the requirement of Junit. However, load the executorService is the target
    // of this test
    @Test
    public void shouldLoadConfiguration() {
        assertThat(executorService, is(notNullValue()));
    }

    @Configuration
    static class TestConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
}