package com.example.sellingservice.Services;

import com.example.sellingservice.Entities.Product;
import com.example.sellingservice.Entities.SellingCompany;
import com.example.sellingservice.Entities.SellingRequest;
import com.example.sellingservice.Entities.SellingRequestMDB;
import com.example.sellingservice.SellingInput;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import jakarta.jms.ObjectMessage;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;


@Path("selling")
@Stateful
@SessionScoped
public class    SellingService  extends Application implements Serializable{
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
    public Response login(SellingInput s, @Context HttpServletRequest request) {
        selling = getSellingByNameFun(s.getUsername());
        if (selling != null) {
            if (selling.getPassword().equals(s.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("selling", selling);
                return Response.status(Response.Status.OK).entity("Selling company logged in").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Incorrect password").build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid username").build();
        }
    }
    public SellingCompany getSellingByNameFun(String username) {
        List<SellingCompany> results = entityManager.createQuery("SELECT u from SellingCompany u WHERE u.username = :username", SellingCompany.class).setParameter("username", username).getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
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
   /* @PUT
    @Path("sellproduct/{id}")
    public String SellProduct(@PathParam("id") int id) {
        Product product = entityManager.find(Product.class, id);
        SellingRequest sellingRequest = new SellingRequest();
        sellingRequest.setProduct(entityManager.find(Product.class,product.getId()));
        sellingRequest.setSellingCompany(entityManager.find(SellingCompany.class,selling.getId()));
        // set the customer name here
        sendSellingRequest(sellingRequest);
        return "selling request sent successfully.";
    }
    @Inject
    private JMSContext context;
    @Resource(mappedName = "jms/sellingRequestsQueue")
    private Queue queue;

    public void sendSellingRequest(SellingRequest sellingRequest) {
        try {
            ObjectMessage message = context.createObjectMessage(sellingRequest);
            context.createProducer().send((Destination) queue, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
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

    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

}
