package com.example.sellingservice.Entities;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    String name;
    int price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="SellingCompany_id")
    private SellingCompany sellingCompany;

    //add
    private boolean isAvailableForSale;
    private int quantity;
    private int quantitySold;

    public Product() {
        quantitySold=0;
        isAvailableForSale=true;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

}
