package com.example.sellingservice.Entities;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    String name;

    int price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="SellingCompany_id")
    private SellingCompany sellingCompany;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ShippingCompany_id")
    private ShippingCompany shippingCompany;

    @ManyToMany(mappedBy = "products")
    Set<CustomerOrder> customerOrders;

    public Set<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(Set<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    //add
    private boolean isAvailableForSale;
    private int quantity;
    private int quantitySold;
    private String imageUrl;
    private boolean shipped;

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product() {
        quantitySold=0;
        isAvailableForSale=true;
    }



    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailableForSale() {
        return isAvailableForSale;
    }

    public void setAvailableForSale(boolean availableForSale) {
        isAvailableForSale = availableForSale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public SellingCompany getSellingCompany() {
        return sellingCompany;
    }

    public void setSellingCompany(SellingCompany sellingCompany) {
        this.sellingCompany = sellingCompany;
    }

    //add
    public boolean getIsAvailableForSale() {
        return isAvailableForSale;
    }

    public void setIsAvailableForSale(boolean isAvailableForSale) {
        this.isAvailableForSale = isAvailableForSale;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold=quantitySold;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public ShippingCompany getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(ShippingCompany shippingCompany) {
        this.shippingCompany = shippingCompany;
    }
}
