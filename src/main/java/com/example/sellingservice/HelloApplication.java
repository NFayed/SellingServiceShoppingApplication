package com.example.sellingservice;

import com.example.sellingservice.Services.CORSFilter;
import com.example.sellingservice.Services.SellingService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(CORSFilter.class);
        classes.add(SellingService.class);

        return classes;
    }
}