package com.example.sellingservice.Services;
import com.example.sellingservice.AdminInput;
import com.example.sellingservice.Entities.Admin;
import com.example.sellingservice.Entities.SellingCompany;
import com.example.sellingservice.Entities.ShippingCompany;
import com.example.sellingservice.SellingInput;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
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

//@Path("shipping")
//@Stateless
public class ShippingService implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    //@Inject
    //ShippingCompany shippingCompany;
    /*@POST
    @Path("login")
    public String login(AdminInput a) {
        shippingCompany = getShippingByName(a.getUsername());
        if(shippingCompany != null)
        {
            if(shippingCompany.getPassword().equals(a.getPassword()))
            {
                return "shipping company logged in";
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
    public ShippingCompany getShippingByName(String username)
    { 	try {
        ShippingCompany shippingCompany = entityManager.createQuery(
                        "SELECT u from ShippingCompany u WHERE u.username = :username", ShippingCompany.class).
                setParameter("username", username).getSingleResult();
        return shippingCompany;
    }
    catch(Exception e){
        return null;
    }
    }*/
}