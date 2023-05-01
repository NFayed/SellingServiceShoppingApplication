package com.example.sellingservice.Services;
import com.example.sellingservice.AdminInput;
import com.example.sellingservice.Entities.CustomerOrder;
import com.example.sellingservice.Entities.ShippingCompany;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Path("shipping")
@Stateless
public class ShippingService implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    static  ShippingCompany shippingCompany;

    @POST
    @Path("login")
    public String login(AdminInput a) {
        shippingCompany = getShippingByName(a.getUsername());
        if (shippingCompany != null) {
            if (shippingCompany.getPassword().equals(a.getPassword())) {
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
            entityManager.merge(customerOrder);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @POST
    @Path("addOrderRequest")
    @Consumes(MediaType.APPLICATION_JSON)
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