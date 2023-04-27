package com.example.sellingservice.Entities;


import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class SessionBean implements Serializable {
    private SellingCompany selling;

    public SellingCompany getSelling() {
        return selling;
    }

    public void setSelling(SellingCompany selling) {
        this.selling = selling;
    }
}
