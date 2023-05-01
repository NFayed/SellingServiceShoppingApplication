package com.example.sellingservice.Entities;

public class SellingOrderOldResult {
    private SellingCompanyOrder sellingCompanyOrder;
    private Product product;

    public SellingOrderOldResult(SellingCompanyOrder sellingCompanyOrder, Product product) {
        this.sellingCompanyOrder = sellingCompanyOrder;
        this.product = product;
    }

    public SellingCompanyOrder getSellingCompanyOrder() {
        return sellingCompanyOrder;
    }

    public void setSellingCompanyOrder(SellingCompanyOrder sellingCompanyOrder) {
        this.sellingCompanyOrder = sellingCompanyOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
