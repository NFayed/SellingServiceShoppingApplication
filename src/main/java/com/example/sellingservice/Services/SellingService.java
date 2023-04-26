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
import java.util.List;

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

    //1. Login into the system using the generated credentials as sent by the admin
    @POST
    //@RolesAllowed({"SellingCompany"})
    @Path("login")
    public String login(SellingInput s, @Context HttpServletRequest request) {
        selling = getSellingByNameFun(s.getUsername());
        if (selling != null) {
            if (selling.getPassword().equals(s.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("selling", selling);
                return "Selling company logged in";
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    public SellingCompany getSellingByNameFun(String username) {
        SellingCompany sellingCompany = entityManager.createQuery("SELECT u from SellingCompany u WHERE u.username = :username", SellingCompany.class).setParameter("username", username).getSingleResult();
        return sellingCompany;

    }

    /////////////////////////////////////////////////////////////
    //2. View products that are currently offered for sale.
    @GET
    @Path("viewproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAvailableProducts() {


        List<Product> products = entityManager.createQuery("SELECT p FROM Product p WHERE p.isAvailableForSale = true", Product.class).getResultList();


        return products;
    }
    /////////////////////////////////////////////////////////////
    //3. View previously sold products, including information about the customers who bought each product and the shipping company.

    //requires a request o customer service to get the customer name and shipping company name and product id


    /////////////////////////////////////////////////////////////
    //4. Add new products.
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

    /////////////////////////////////////////////////////////////
    //needed by other services
    @PUT
    @Path("sellproduct/{id}")
    public String Sell(@PathParam("id") int id) {
        Product product = entityManager.find(Product.class, id);
        product.setQuantity(product.getQuantity() - 1);
        if (product.getQuantity() == 0) {
            product.setIsAvailableForSale(false);
        }
        entityManager.merge(product);
        return "product sold successfully.";
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public SellingCompany getSellingByName(@PathParam("username") String username) {
        try {
            return entityManager.createQuery("SELECT u from SellingCompany u WHERE u.username = :username", SellingCompany.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}
