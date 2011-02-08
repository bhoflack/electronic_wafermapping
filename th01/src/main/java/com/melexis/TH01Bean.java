package com.melexis;

import org.apache.servicemix.jbi.listener.MessageExchangeListener;

import javax.annotation.Resource;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;

public class TH01Bean implements MessageExchangeListener {

    @Resource private DeliveryChannel channel;

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        System.out.println("Received convert job for lot: " + exchange);
        exchange.setStatus(ExchangeStatus.DONE);
    }

}
