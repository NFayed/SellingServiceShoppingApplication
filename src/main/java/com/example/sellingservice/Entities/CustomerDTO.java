package com.example.sellingservice.Entities;

public class CustomerDTO {

    private Long id;
    String name;

    int soldproductid;

    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
