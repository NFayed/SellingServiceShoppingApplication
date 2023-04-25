package com.example.sellingservice.Entities;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name="admin")
@Stateless
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


    @OneToMany(mappedBy="admin")
    Set<SellingCompany> sellingCompanies;

    //ArrayList<Order> currentOrders;

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
}
