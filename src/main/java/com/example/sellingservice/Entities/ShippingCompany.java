package com.example.sellingservice.Entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Shipping Company")
public class ShippingCompany implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String username;
    private String password;

    @ElementCollection
    @CollectionTable(name = "shipping_company_locations", joinColumns = @JoinColumn(name = "shipping_company_id"))
    @Column(name = "location")
    private List<String> locations;

    @OneToMany(mappedBy = "shippingCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="admin_id")
    private Admin admin;
    public ShippingCompany() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
