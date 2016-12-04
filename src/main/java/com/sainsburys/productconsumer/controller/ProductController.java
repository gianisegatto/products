package com.sainsburys.productconsumer.controller;

import com.sainsburys.productconsumer.domain.Product;
import com.sainsburys.productconsumer.domain.Results;
import com.sainsburys.productconsumer.service.ProductService;
import com.sainsburys.productconsumer.service.ProductServiceAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Optional;

/**
 * Controller to provider services to list the products
 * This controller prodives two services and both returns the same results.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductServiceAsync productServiceAsync;

    /**
     * Call the Sainsbury's products test page and return a list of the products inside the page
     * This service is a blocking style and execute everything in a sequence.
     * @return Results object with list of products and the total sum price of all products
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> listProducts() {

        Optional<Results> results = productService.listProducts();

        return results.map(results1 -> results1.getProducts().size() > 0 ? new ResponseEntity(results1, HttpStatus.OK) : null)
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    /**
     * Call the Sainsbury's products test page and return a list of the products inside the page
     * This service is a non-blocking style and execute the process in parallel.
     * @return Results object with list of products and the total sum price of all products
     */
    @RequestMapping(value = "/async",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<Results>> listProductsAsync() {

        return productServiceAsync.listProducts();
    }
}
