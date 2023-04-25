package com.example.sellingservice.Services;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
@Path("test")
public class testService {
    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}
