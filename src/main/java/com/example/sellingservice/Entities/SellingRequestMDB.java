package com.example.sellingservice.Entities;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.Queue;
@MessageDriven(
        activationConfig = {
                @jakarta.ejb.ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @jakarta.ejb.ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/orderQueue")
        },
        mappedName = "java:/jms/queue/orderQueue", name = "SellingRequestMDB")
public class SellingRequestMDB implements MessageListener {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager entityManager = entityManagerFactory.createEntityManager();



//@Override
//public void onMessage(Message message) {
//    try {
//        String orderRequest = message.getBody(String.class);
//        int productId = Integer.parseInt(orderRequest);
//        System.out.println("Received message: " + productId);
//        Product product = entityManager.find(Product.class, productId);
//
//        if (product != null) {
//            System.out.println("Product: " + product.name);
//            product.setQuantity(product.getQuantity() - 1);
//            if (product.getQuantity() == 0) {
//                product.setIsAvailableForSale(false);
//            }
//            entityManager.merge(product);
//        } else {
//            System.out.println("Product not found for ID: " + productId);
//        }
//    } catch (JMSException e) {
//        e.printStackTrace();
//    }
//}
@Override
public void onMessage(Message message) {
    try {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            customerAndOrderId customerAndOrderId = (customerAndOrderId) objectMessage.getObject();

            // Extract orderId and parse it as productId
            String email = customerAndOrderId.getEmail();
            int productId = customerAndOrderId.getOrderId();

            System.out.println("Received message: " + productId);
            Product product = entityManager.find(Product.class, productId);

            if (product != null) {
                System.out.println("Product: " + product.name);
                product.setQuantity(product.getQuantity() - 1);
                if (product.getQuantity() == 0) {
                    product.setIsAvailableForSale(false);
                }
                entityManager.merge(product);
            } else {
                System.out.println("Product not found for ID: " + productId);
            }
        } else {
            System.out.println("Invalid message type received.");
        }
    } catch (JMSException e) {
        e.printStackTrace();
    }
}


}

