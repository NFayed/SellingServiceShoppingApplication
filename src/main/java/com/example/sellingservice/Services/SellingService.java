package com.example.sellingservice.Services;

import com.example.sellingservice.Entities.Admin;
import com.example.sellingservice.Entities.SellingCompany;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;

@Path("selling")
@Stateful
public class SellingService implements Serializable {
    //@PersistenceContext(unitName = "default")
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addSelling(SellingCompany a) {
        entityManager.persist(a);
        return "Selling company added successfully.";
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public SellingCompany getSellingByName(@PathParam("username") String username) {
        try {
            return entityManager.createQuery(
                            "SELECT u from SellingCompany u WHERE u.username = :username", SellingCompany.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }


}
