package com.example.sellingservice.Entities;

import jakarta.annotation.Resource;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

//@MessageDriven(mappedName = "jms/shippingRequestQueue")
//public class ShippingRequest implements MessageListener {
public class ShippingRequest  {
    /*
    @JMSConnectionFactory("jms/ConnectionFactory")
    private JMSContext context;

    @Resource(lookup = "jms/shippingResponseQueue")
    private Queue responseQueue;

    @Override
    public void onMessage(Message message) {
        try {
            // Process shipping request
            String orderId = message.getStringProperty("orderId");
            String location = message.getStringProperty("location");
            // ...process shipping request here...

            // Send shipping response
            Message response = context.createMessage();
            response.setStringProperty("orderId", orderId);
            response.setBooleanProperty("success", true);
            context.createProducer().send(responseQueue, response);
        } catch (JMSException e) {
            throw new JMSRuntimeException(e.getMessage(), e.getErrorCode(), e);
        }
    }*/
}