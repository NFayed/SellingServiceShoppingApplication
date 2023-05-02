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
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.*;


    @Path("admin")
    @Singleton
    public class AdminService  implements Serializable {
        //@PersistenceContext(unitName = "default")
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        int number=1;
        Admin admin = Admin.getInstance();

        public Admin getAdminInstance() {
            return Admin.getInstance();
        }
        @POST
        @Path("login")
        public String login(AdminInput a) {
            //Admin admin = Admin.getInstance();
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
    public SellingCompany getSellingCompanyByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT sc FROM SellingCompany sc WHERE sc.username = :username", SellingCompany.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    @POST
    @Path("createselling")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSelling(SellingInput sellingInput) {
        SellingCompany s1 = new SellingCompany();
        s1.setPassword(RandGeneratedStr());
        SellingCompany existingSellingCompany = getSellingCompanyByUsername(sellingInput.getUsername());
        if (existingSellingCompany == null) {
            s1.setUsername(sellingInput.getUsername());
            s1.setRole("selling");
            s1.setAdmin(entityManager.find(Admin.class,1));
            entityManager.persist(s1);
            return "selling company added successfully.";
        }
        else {
            return "username already exists";
        }

    }


        @POST
        @Path("createshipping")
        @Consumes(MediaType.APPLICATION_JSON)
        public String addShipping(ShippingCompany a) {
            ShippingCompany s1 = getShippingByNameFun(a.getUsername());
            if(s1== null) {
                a.setAdmin(admin);
                entityManager.persist(a);
                return "shipping company added successfully.";
            }
            return "shipping company already exists!";
        }

        @GET
        @Path("getallselling")
        @Produces(MediaType.APPLICATION_JSON)
        public List<SellingCompany> getAllSellingCompaniesByAdminId() {
            TypedQuery<SellingCompany> query = entityManager.createQuery(
                    "SELECT sc FROM SellingCompany sc WHERE sc.admin.id = :adminId", SellingCompany.class);
            query.setParameter("adminId", admin.getId());
            return query.getResultList();
        }
        @GET
        @Path("getallshipping")
        @Produces(MediaType.APPLICATION_JSON)
        public List<ShippingCompany> getAllShippingCompaniesByAdminId() {
            try {
                TypedQuery<ShippingCompany> query = entityManager.createQuery(
                        "SELECT sc FROM ShippingCompany sc WHERE sc.admin.id = :adminId", ShippingCompany.class);
                query.setParameter("adminId", admin.getId());
                return query.getResultList();
            } catch (Exception e) {
                // Handle the exception or log the error message
                e.printStackTrace();
                // Return an empty list or a custom error response
                return new ArrayList<>();
            }
        }
        @GET
        @Path("getallcustomers")
        @Produces(MediaType.APPLICATION_JSON)
        public List<Object> getAllCustomersByAdminId() {
            String externalApiUrl = "https://customerservice-lepf.onrender.com/getAllCustomers"; // Replace this with the actual API URL

            Client client = ClientBuilder.newClient();
            List<Object> customers = client.target(externalApiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Object>>() {});

            client.close();
            return customers;
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
        static String RandGeneratedStr(){
            String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
            String numbers = "0123456789";
            String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            int length = 8;

            for(int i = 0; i < length; i++) {

                int index = random.nextInt(alphaNumeric.length());

                char randomChar = alphaNumeric.charAt(index);

                sb.append(randomChar);
            }

            String randomString = sb.toString();
            return randomString;
            //System.out.println("Random String is: " + randomString);

        }
    }

