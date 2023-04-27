package com.example.sellingservice.Entities;

import jakarta.persistence.*;

import java.io.Serializable;

public class SellingRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private int productId;
    private int sellingCompanyId;
    private int customerId;
    private int quantity;
    private double price;


    public SellingRequest() {
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SellingRequest [product=" + productId + ", quantity=" + quantity + ", price=" + price + "]";
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellingCompanyId() {
        return sellingCompanyId;
    }

    public void setSellingCompanyId(int sellingCompanyId) {
        this.sellingCompanyId = sellingCompanyId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
