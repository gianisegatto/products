package com.sainsburys.productconsumer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the product that will be returned to the client
 */
public class Product {

    private String title;
    private String size;
    @JsonProperty("unit_price")
    private Float unitPrice;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
