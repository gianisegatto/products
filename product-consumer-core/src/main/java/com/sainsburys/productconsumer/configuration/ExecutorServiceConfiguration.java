package com.sainsburys.productconsumer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to create the ExecutorService bean to be used on the @ProductServiceAsync
 */
@Configuration
public class ExecutorServiceConfiguration {

    private int corePoolSize;
    private int maximumPoolSize;
    private int keepAliveTime;

    public ExecutorServiceConfiguration(@Value("${executor.service.corePoolSize}")
                                        int corePoolSize,
                                        @Value("${executor.service.maximumPoolSize}")
                                        int maximumPoolSize,
                                        @Value("${executor.service.keepAliveTime}")
                                        int keepAliveTime) {

        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maximumPoolSize));
    }
}
