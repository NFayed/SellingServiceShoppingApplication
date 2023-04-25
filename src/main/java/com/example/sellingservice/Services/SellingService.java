package com.example.sellingservice.Services;
import com.example.sellingservice.Entities.Product;
import com.example.sellingservice.Entities.SellingCompany;
import com.example.sellingservice.SellingInput;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;

@Path("selling")
@Stateful
@SessionScoped
public class SellingService implements Serializable {
    //@PersistenceContext(unitName = "default")
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    SellingCompany selling;

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addSelling(SellingCompany a) {
        entityManager.persist(a);
        return "Selling company added successfully.";
    }
   @POST
   //@RolesAllowed({"SellingCompany"})
   @Path("login")
    public String login(SellingInput s, @Context HttpServletRequest request) {
        selling = getSellingByNameFun(s.getUsername());
        if(selling != null)
        {
            if(selling.getPassword().equals(s.getPassword()))
            {
                HttpSession session = request.getSession(true);
                session.setAttribute("selling", selling);
                return "Selling company logged in";
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
    @Path("addproduct")
    public String addProduct(Product product, @Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (selling != null) {
                selling.getProducts().add(product);
                product.setSellingCompany(selling);
                entityManager.persist(product);
                return "product added successfully.";
            }
        }
        return "invalid product.";
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


}
