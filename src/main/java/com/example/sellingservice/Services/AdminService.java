package com.example.sellingservice.Services;

import com.example.sellingservice.AdminInput;
import com.example.sellingservice.Entities.*;
import com.example.sellingservice.SellingInput;
import jakarta.ejb.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("admin")
@Singleton
public class AdminService  implements Serializable {
    //@PersistenceContext(unitName = "default")
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    @Inject
    Admin admin;
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addAdmin(Admin a) {
        Admin a1 = getAdminByNameFun(a.getUsername());
        if(a1== null) {
            entityManager.persist(a);
            return "Admin added successfully.";
        }
        return "Admin already exists!";
    }
    @POST
    @Path("login")
    public String login(AdminInput a) {
        admin = getAdminByNameFun(a.getUsername());
        if(admin != null)
        {
            if(admin.getPassword().equals(a.getPassword()))
            {
                return "Admin logged in";
            }
            else
            {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }
        else
        {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Path("createselling")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addSelling(SellingCompany a) {
        SellingCompany s1 = getSellingByNameFun(a.getUsername());
        if(s1== null) {
            entityManager.persist(a);
            return "selling company added successfully.";
        }
        return "selling company already exists!";
    }
    @POST
    @Path("createshipping/{adminId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addShipping(ShippingCompany a, @PathParam("adminId") String adminId) {
        ShippingCompany s1 = getShippingByNameFun(a.getUsername());
        if(s1== null) {
            a.setAdmin(entityManager.find(Admin.class,adminId));
            entityManager.persist(a);
            return "shipping company added successfully.";
        }
        return "shipping company already exists!";
    }

    @GET
    @Path("getallselling/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SellingCompany> getAllSellingCompaniesByAdminId(@PathParam("adminId") String adminId) {
        TypedQuery<SellingCompany> query = entityManager.createQuery(
                "SELECT sc FROM SellingCompany sc WHERE sc.admin.id = :adminId", SellingCompany.class);
        query.setParameter("adminId", Long.parseLong(adminId));
        return query.getResultList();
    }
    @GET
    @Path("getallshipping/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShippingCompany> getAllShippingCompaniesByAdminId(@PathParam("adminId") String adminId) {
        TypedQuery<ShippingCompany> query = entityManager.createQuery(
                "SELECT sc FROM ShippingCompany sc WHERE sc.admin.id = :adminId", ShippingCompany.class);
        query.setParameter("adminId", Long.parseLong(adminId));
        return query.getResultList();
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

    public Admin getAdminByNameFun(String username) {
        try {
            return entityManager.createQuery(
                            "SELECT u from Admin u WHERE u.username = :username", Admin.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public SellingCompany getSellingByNameFun(String username)
    { 	try {
        SellingCompany sellingCompany = entityManager.createQuery(
                        "SELECT u from SellingCompany u WHERE u.username = :username", SellingCompany.class).
                setParameter("username", username).getSingleResult();
        return sellingCompany;
    }
    catch(Exception e){
        return null;
    }
    }
    public ShippingCompany getShippingByNameFun(String username)
    { 	try {
        ShippingCompany shippingCompany = entityManager.createQuery(
                        "SELECT u from ShippingCompany u WHERE u.username = :username", ShippingCompany.class).
                setParameter("username", username).getSingleResult();
        return shippingCompany;
    }
    catch(Exception e){
        return null;
    }
    }

}