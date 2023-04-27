package com.example.sellingservice.Entities;

import jakarta.persistence.*;

import java.io.Serializable;
@Entity
@Table(name="Shipping Company")
public class ShippingCompany implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String role;
    private String username;
    private String password;
    private String location;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="admin_id")
    private Admin admin;
    public ShippingCompany() {
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
