package com.example.sellingservice.Services;

import com.example.sellingservice.Entities.Admin;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;

@Path("admin")
@Stateful
public class AdminService implements Serializable {
    //@PersistenceContext(unitName = "default")
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addAdmin(Admin a) {
        entityManager.persist(a);
        return "Admin added successfully.";
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Admin getAdminByName(@PathParam("username") String username) {
        try {
            return entityManager.createQuery(
                            "SELECT u from Admin u WHERE u.username = :username", Admin.class)
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
