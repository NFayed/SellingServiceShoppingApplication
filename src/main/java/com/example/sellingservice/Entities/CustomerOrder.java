package com.example.sellingservice.Entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="CustomerOrder")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class CustomerOrder implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    boolean shipped;
    int total;
    boolean completed;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    String _id;
    private String customerName;

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    String customerAddress;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shippingCompany_id")
//    @JsonIgnore
//    @Transient
    private ShippingCompany shippingCompany;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sellingCompany_id")
//    @JsonIgnore
//    @Transient
    private SellingCompany sellingCompany;

    @ManyToMany
    @JoinTable(
            name = "ProductsInOrder",
            joinColumns = @JoinColumn(name = "customerorder_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    //@JsonIgnore
    Set<Product> products = new HashSet<>();
    public CustomerOrder() {}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    public ShippingCompany getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(ShippingCompany shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public SellingCompany getSellingCompany() {
        return sellingCompany;
    }

    public void setSellingCompany(SellingCompany sellingCompany) {
        this.sellingCompany = sellingCompany;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        completed = completed;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}