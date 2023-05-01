package com.example.sellingservice.Entities;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;
import jakarta.persistence.*;

import java.util.List;
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


@Override
public void onMessage(Message message) {
    try {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            System.out.println("Received message: " + objectMessage.getObject());

            CustomerOrder customerOrder = (CustomerOrder) objectMessage.getObject();

            System.out.println("CustomerOrder: " + customerOrder);
            System.out.println("Received message: " + customerOrder.getCustomerName() + " " + customerOrder.getCustomerAddress());
            // Extract orderId and parse it as productId
            String name = customerOrder.getCustomerName();
            String address = customerOrder.getCustomerAddress();


            System.out.println("Received message: " + name + " " + address);
            for (Product product : customerOrder.getProducts()) {
                System.out.println("Product: " + product.name);
                System.out.println("Product quantity: " + product.getQuantity());
                if (product != null) {
                    System.out.println("Product: " + product.name);
                    product.setQuantity(product.getQuantity() - 1);
                    if (product.getQuantity() == 0) {
                        product.setIsAvailableForSale(false);
                    }
                    String customerAddress = customerOrder.getCustomerAddress();
                    ShippingCompany shippingCompany = checkShippingAvailability(customerAddress);
                    if (shippingCompany != null) {
                        // Assign the shipping company to the order and update it
                        customerOrder.setShippingCompany(shippingCompany);
                        entityManager.merge(customerOrder);
                        entityManager.merge(product);
                        entityManager.flush();
                    } else {
                        System.out.println("No shipping company available for address: " + customerAddress);
                    }

                }

                else {
                        System.out.println("Product not found for ID: " + product.getId());
                    }
            }

            }


         else {
            System.out.println("Invalid message type received.");
        }
    } catch (JMSException e) {
        e.printStackTrace();
    }
}

    public ShippingCompany checkShippingAvailability(String location) {
        try {
            TypedQuery<ShippingCompany> query = entityManager.createQuery(
                    "SELECT sc FROM ShippingCompany sc JOIN sc.locations l WHERE l = :location", ShippingCompany.class);
            query.setParameter("location", location);
            List<ShippingCompany> shippingCompanies = query.getResultList();

            if (!shippingCompanies.isEmpty()) {
                // Return the first available shipping company
                return shippingCompanies.get(0);
            } else {
                // No shipping company available for the specified location
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}

