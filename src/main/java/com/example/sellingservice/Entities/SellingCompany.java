package com.example.sellingservice.Entities;

import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name="sellingCompany")
@Stateful
public class SellingCompany {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String role;
    private String username;
    private String password;
    @OneToMany(mappedBy="sellingCompany")
    ArrayList<Product> products;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
