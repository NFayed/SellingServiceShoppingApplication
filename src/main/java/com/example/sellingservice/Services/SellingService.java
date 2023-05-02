package com.example.sellingservice.Services;

import com.example.sellingservice.Entities.*;


import com.example.sellingservice.SellingInput;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.jms.*;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.naming.InitialContext;
//import javax.naming.Context;
import java.io.Serializable;
import java.util.List;


@Path("selling")
@Stateful
@SessionScoped
public class    SellingService  extends Application implements Serializable{
    //@PersistenceContext(unitName = "default")
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Resource(mappedName = "java:/jms/queue/orderQueue")
    private Queue queue;


    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addSelling(SellingCompany a) {
        entityManager.persist(a);
        return "Selling company added successfully.";
    }

    //1. Login into the system using the generated credentials as sent by the admin
    @POST
    @Path("login")
    public Response login(SellingInput s,  @Context HttpServletRequest request) {
        SellingCompany selling;
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
            System.out.println("Session is not null");
            System.out.println("Session ID: " + session.getId());
            SellingCompany selling2 = (SellingCompany) session.getAttribute("selling");
            if (selling2 != null) {
                System.out.println("Selling company: " + selling2);
                selling2.addProduct(product);
                product.setSellingCompany(selling2);
                entityManager.persist(product);
                System.out.println("Product added with ID: " + product.getId());
                return "Product added successfully.";
            } else {
                System.out.println("Selling company is null");
            }
        }
        return "not logged in.";
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

    @PUT
    @Path("/sendOrder")
    public void submitOrder(CustomerOrder customerOrder) {
        try {
            System.out.println("Sending order request");
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(this.queue);

            // Create an ObjectMessage and set the customerAndOrderId object
            ObjectMessage message = session.createObjectMessage();
            message.setObject(customerOrder);

            System.out.println("Sending the following order request: " + customerOrder.getTotal());
            producer.send(message);
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @GET
    @Path("/getOldOrders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SellingOrderOldResult> getOldOrders(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("Session is not null");
            System.out.println("Session ID: " + session.getId());
            SellingCompany sellingCompany = (SellingCompany) session.getAttribute("selling");
            if (sellingCompany != null) {
                System.out.println("Selling company: " + sellingCompany);

                TypedQuery<SellingOrderOldResult> query = entityManager.createQuery(
                                "SELECT new com.example.sellingservice.Entities.SellingOrderOldResult(sco, p) FROM SellingCompanyOrder sco JOIN Product p ON sco.productId = p.id WHERE p.sellingCompany = :sellingCompany",
                                SellingOrderOldResult.class)
                        .setParameter("sellingCompany", sellingCompany);

                List<SellingOrderOldResult> results = query.getResultList();

                if (!results.isEmpty()) {
                    return results;
                } else {
                    System.out.println("No old orders found");
                }
            } else {
                System.out.println("Selling company is null");
            }
        }
        return null;
    }

    @GET
    @Path("/getSellingCompanyProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getSellingCompanyProducts(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("Session is not null");
            System.out.println("Session ID: " + session.getId());
            SellingCompany sellingCompany = (SellingCompany) session.getAttribute("selling");
            if (sellingCompany != null) {
                System.out.println("Selling company: " + sellingCompany);

                TypedQuery<Product> query = entityManager.createQuery(
                                "SELECT p FROM Product p WHERE p.sellingCompany = :sellingCompany",
                                Product.class)
                        .setParameter("sellingCompany", sellingCompany);

                List<Product> results = query.getResultList();

                if (!results.isEmpty()) {
                    return results;
                } else {
                    System.out.println("No products found");
                }
            } else {
                System.out.println("Selling company is null");
            }
        }
        return null;
    }



    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

}
