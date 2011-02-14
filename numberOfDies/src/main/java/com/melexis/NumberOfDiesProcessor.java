package com.melexis;

import org.apache.log4j.Logger;
import org.apache.servicemix.common.util.MessageUtil;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;

import javax.annotation.Resource;
import javax.jbi.messaging.*;

public class NumberOfDiesProcessor implements MessageExchangeListener {

    private final static Logger log = Logger.getLogger(NumberOfDiesProcessor.class);

    @Resource private DeliveryChannel channel;

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        if (exchange.getStatus() == ExchangeStatus.ACTIVE) {
            log.info("Received job to extract number of dies.");

            final InOut inOut = (InOut) exchange;
            final NormalizedMessage msg = inOut.getInMessage();

            final String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<event>\n" +
                "<type>number_of_dies</type>\n" +
                "<lotname>A84001</lotname>" +
                "<item>201234500</item>" +
                "<when>20110207-16:02</when>" +
                "<operator>brh</operator>" +
                "<probing-site>IEPER</probing-site>" +
                "<wafers>" +
                "<wafer id=\"1\" passdies=\"55\" />" +
                "<wafer id=\"2\" passdies=\"88\" />" +
                "</wafers>" +
                "</event>";
            msg.setContent(new StringSource(contents));
            MessageUtil.transferInToOut(inOut, inOut);
            channel.send(inOut);
        } else {
            log.warn("Received exchange,  but status is not ACTIVE." + exchange);
        }
    }

}
