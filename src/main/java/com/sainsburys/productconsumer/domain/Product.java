package com.sainsburys.productconsumer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Supplier;

public class Product {

    private String title;
    private String size;
    @JsonProperty("unit_price")
    private Float unitPrice;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
