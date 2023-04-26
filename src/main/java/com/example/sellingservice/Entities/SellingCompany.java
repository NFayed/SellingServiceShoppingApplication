package com.example.sellingservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="sellingCompany")
@Stateful
@SessionScoped
@JsonIgnoreProperties("products")
public class SellingCompany implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String role;
    private String username;
    private String password;

    @OneToMany(mappedBy="sellingCompany",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Product> products;


    @ManyToOne(fetch = FetchType.EAGER)
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    //add
   public void addProduct(Product product){
        products.add(product);
    }


}


