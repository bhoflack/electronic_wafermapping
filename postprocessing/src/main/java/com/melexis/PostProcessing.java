package com.melexis;

import org.apache.log4j.Logger;
import org.apache.servicemix.common.util.MessageUtil;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;
import org.apache.servicemix.util.jaf.ByteArrayDataSource;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.jbi.messaging.*;

import java.io.IOException;

import static org.apache.commons.io.IOUtils.toByteArray;

public class PostProcessing implements MessageExchangeListener {

    private final static Logger log = Logger.getLogger(PostProcessing.class);

    @Resource private DeliveryChannel channel;

    public void onMessageExchange(final MessageExchange exchange) throws MessagingException {
        try {
            if (exchange.getStatus() == ExchangeStatus.ACTIVE) {
                if (log.isDebugEnabled()) {
                    log.debug("Received postprocessing job for lot: " + exchange);
                }

                final InOut inOut = (InOut) exchange;
                final NormalizedMessage msg = inOut.getInMessage();

                final String out = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><event>" +
                    "<type>performed_postprocessing</type>" +
                    "<lotname>A84001</lotname>" +
                    "<item>201234500</item>" +
                    "<when>20110207-16:02</when>" +
                    "<operator>brh</operator>" +
                    "<probing-site>IEPER</probing-site>" +
                    "<wafers>" +
                    "<wafer>1</wafer>" +
                    "<wafer>2</wafer>" +
                    "</wafers>" +
                    "</event>";

                msg.setContent(new StringSource(out));
                msg.addAttachment("A84001-1.th01", new DataHandler(new ByteArrayDataSource(postprocessWafermap(), "bin/th01")));
                msg.addAttachment("A84001-1.th01", new DataHandler(new ByteArrayDataSource(postprocessWafermap(), "bin/th01")));

                MessageUtil.transferInToOut(inOut, inOut);
                channel.send(inOut);
            } else {
                log.warn("Received exchange,  but status is not ACTIVE." + exchange);
            }
        } catch (IOException e) {
            throw new MessagingException("Exception when handling message", e);
        }
    }

    private final static byte[] postprocessWafermap() throws IOException {
        // For now read a static wafermap from the resources.
        return toByteArray(PostProcessing.class.getClassLoader().getResourceAsStream("test_wafermap1.th01"));
    }
}