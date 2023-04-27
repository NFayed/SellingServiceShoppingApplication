package com.example.sellingservice.Entities;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Queue;
public class SellingRequestMDB {
//@MessageDriven(mappedName = "jms/sellingRequestsQueue")
/*
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = "jms/sellingRequestsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
 */
/*
@JMSDestinationDefinition(
        name = "java:/jms/sellingRequestsQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "sellingRequestsQueue"
)
@MessageDriven(
        name = "SellingRequestMDB",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/sellingRequestsQueue"),
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
        }
)
public class SellingRequestMDB implements MessageListener {
    //public class SellingRequestMDB {
    @Inject
    private JMSContext context;

    @Inject
    @JMSConnectionFactory("java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Resource(mappedName = "jms/sellingRequestsQueue")
    private Queue queue;
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                SellingRequest sellingRequest = (SellingRequest) ((ObjectMessage) message).getObject();
                Long productId = (long) sellingRequest.getProductId();
                //int quantity = sellingRequest.getQuantity();
                //double price = sellingRequest.getPrice();
                Product product = entityManager.find(Product.class, productId);
                product.setQuantity(product.getQuantity() - 1);
                if (product.getQuantity() == 0) {
                    product.setIsAvailableForSale(false);
                }
                entityManager.merge(product);
                System.out.println("Selling request processed: " + sellingRequest);
            } else {
                System.out.println("Unexpected message type received: " + message.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
