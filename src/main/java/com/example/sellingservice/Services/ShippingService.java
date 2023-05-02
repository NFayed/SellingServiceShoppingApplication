package com.example.sellingservice.Services;
import com.example.sellingservice.AdminInput;
import com.example.sellingservice.Entities.CustomerOrder;
import com.example.sellingservice.Entities.Product;
import com.example.sellingservice.Entities.SellingCompanyOrder;
import com.example.sellingservice.Entities.ShippingCompany;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Path("shipping")
@Stateful
public class ShippingService implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();


    @POST
    @Path("login")
    public long login(AdminInput a) {
        ShippingCompany shippingCompany;
        shippingCompany = getShippingByName(a.getUsername());
        if (shippingCompany != null) {
            if (shippingCompany.getPassword().equals(a.getPassword())) {
                return shippingCompany.getId(); // Return the shipping company id
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
            String externalApiUrl = "http://localhost:4000/shipOrder"; // Replace this with the actual API URL
            Client client = ClientBuilder.newClient();
            ObjectMapper objectMapper = new ObjectMapper();
            String customerOrderJson = objectMapper.writeValueAsString(customerOrder);
            System.out.println("CustomerOrder: " + customerOrderJson);
            System.out.println("External API URL: " + externalApiUrl);

            // Send the POST request with the CustomerOrder in the body
            Response response = client.target(externalApiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(customerOrderJson, MediaType.APPLICATION_JSON));
            System.out.println("Response: " + response.getStatus());

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



    public Response addOrderRequest(CustomerOrder customerOrder) {
        try {
//            customerOrder.getProducts().forEach(product -> {
//                product.setShippingCompany(shippingCompany);
//                System.out.println("Product: " + product.getName());
//                product.setQuantity(product.getQuantity() - 1);
//                if (product.getQuantity() == 0) {
//                    product.setIsAvailableForSale(false);
//                }
//                entityManager.merge(product);
//            });
//            customerOrder.setShippingCompany(shippingCompany);
            customerOrder.setCompleted(true);
            customerOrder.setShipped(false);
            entityManager.getTransaction().begin();
            entityManager.persist(customerOrder);
            entityManager.getTransaction().commit();
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("getOrderRequests/{shippingCompanyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerOrder> getOrderRequests(@PathParam("shippingCompanyId") long shippingCompanyId) {
        try {
            ShippingCompany shippingCompany = entityManager.find(ShippingCompany.class, shippingCompanyId);

            if (shippingCompany == null) {
                return Collections.emptyList();
            }

            TypedQuery<CustomerOrder> query = entityManager.createQuery(
                            "SELECT u from CustomerOrder u WHERE u.shippingCompany = :shippingCompany AND u.shipped = false",
                            CustomerOrder.class)
                    .setParameter("shippingCompany", shippingCompany);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}