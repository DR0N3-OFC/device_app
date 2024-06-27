package br.edu.utfpr.deviceapi.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceProducer {
    @Autowired private AmqpTemplate amqptemplate;

    public void sendMessage(String message) {
        amqptemplate.convertAndSend(
            "device-exchange",
            "device-routing-queue",
            message
        );
    }
}
