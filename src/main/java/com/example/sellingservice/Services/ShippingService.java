package com.example.sellingservice.Services;
import com.example.sellingservice.AdminInput;
import com.example.sellingservice.Entities.CustomerOrder;
import com.example.sellingservice.Entities.Product;
import com.example.sellingservice.Entities.SellingCompanyOrder;
import com.example.sellingservice.Entities.ShippingCompany;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Path("shipping")
@Stateful
@SessionScoped
public class ShippingService implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();


    @POST
    @Path("login")
    public String login(AdminInput a,@Context HttpServletRequest request) {
        ShippingCompany shippingCompany ;

        shippingCompany = getShippingByName(a.getUsername());
        if (shippingCompany != null) {
            if (shippingCompany.getPassword().equals(a.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("shipping", shippingCompany);
                return "shipping company logged in";
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    public ShippingCompany getShippingByName(String username) {
        try {
            ShippingCompany shippingCompany = entityManager.createQuery(
                            "SELECT u from ShippingCompany u WHERE u.username = :username", ShippingCompany.class).
                    setParameter("username", username).getSingleResult();
            return shippingCompany;
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("shipOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response shipOrder(CustomerOrder customerOrder) {
        try {
            customerOrder.setShipped(true);
            String externalApiUrl = "https://customerservice-lepf.onrender.com/shipOrder"; // Replace this with the actual API URL
            Client client = ClientBuilder.newClient();
            ObjectMapper objectMapper = new ObjectMapper();
            String customerOrderJson = objectMapper.writeValueAsString(customerOrder);

            // Send the POST request with the CustomerOrder in the body
            Response response = client.target(externalApiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(customerOrderJson, MediaType.APPLICATION_JSON));
            for (Product product : customerOrder.getProducts()) {
                SellingCompanyOrder sellingCompanyOrder = new SellingCompanyOrder();
                sellingCompanyOrder.setShippingCompanyName(customerOrder.getShippingCompany().getUsername());
                sellingCompanyOrder.setCustomerName(customerOrder.getCustomerName());
                int productId = product.getId();
                sellingCompanyOrder.setProductId(productId);
                entityManager.merge(sellingCompanyOrder);
            }

            entityManager.merge(customerOrder);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



    @GET
    @Path("getOrderRequests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerOrder> getOrderRequests( @Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ShippingCompany shippingCompany = (ShippingCompany) session.getAttribute("shipping");
            if (shippingCompany != null) {
                TypedQuery<CustomerOrder> query = entityManager.createQuery(
                                "SELECT u from CustomerOrder u WHERE u.shippingCompany = :shippingCompany AND u.shipped = false",
                                CustomerOrder.class)
                        .setParameter("shippingCompany", shippingCompany);

                return query.getResultList();
            }
        }
        return Collections.emptyList();
    }



}