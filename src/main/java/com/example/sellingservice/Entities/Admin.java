package com.example.sellingservice.Entities;

import com.example.sellingservice.Entities.SellingCompany;
import com.example.sellingservice.Entities.ShippingCompany;
import jakarta.ejb.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name="admin")
@Singleton
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String role;
    private String username;
    private String password;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @OneToMany(mappedBy="admin",fetch = FetchType.EAGER)
    @JsonIgnore
    Set<SellingCompany> sellingCompanies;
    @OneToMany(mappedBy="admin",fetch = FetchType.EAGER)
    @JsonIgnore
    Set<ShippingCompany> shippingCompanies;


    public Admin() {
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


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<SellingCompany> getSellingCompanies() {
        return sellingCompanies;
    }

    public void setSellingCompanies(Set<SellingCompany> sellingCompanies) {
        this.sellingCompanies = sellingCompanies;
    }

    public Set<ShippingCompany> getShippingCompanies() {
        return shippingCompanies;
    }

    public void setShippingCompanies(Set<ShippingCompany> shippingCompanies) {
        this.shippingCompanies = shippingCompanies;
    }


}