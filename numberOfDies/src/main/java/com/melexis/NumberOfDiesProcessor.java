package com.melexis;

import com.melexis.th01.TH01WaferMap;
import com.melexis.th01.exception.Th01Exception;
import org.apache.log4j.Logger;
import org.apache.servicemix.common.util.MessageUtil;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.jbi.messaging.*;
import java.io.IOException;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.io.IOUtils.toByteArray;

public class NumberOfDiesProcessor implements MessageExchangeListener {

    private final static Logger log = Logger.getLogger(NumberOfDiesProcessor.class);

    @Resource private DeliveryChannel channel;

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        try {
        if (exchange.getStatus() == ExchangeStatus.ACTIVE) {
            log.info("Received job to extract number of dies.");

            final InOut inOut = (InOut) exchange;
            final NormalizedMessage msg = inOut.getInMessage();

            updateWithNumberOfGoodDies(msg);
            MessageUtil.transferInToOut(inOut, inOut);
            channel.send(inOut);
        } else {
            log.debug("Received exchange,  but status is not ACTIVE." + exchange);
        }
        } catch(Exception e) {
            throw new MessagingException("An exception occured while processing message", e);
        }
    }

    private static void updateWithNumberOfGoodDies(final NormalizedMessage msg) throws MessagingException, Th01Exception, IOException {
        final StringSource contents = (StringSource) msg.getContent();

        for (final String name : (Set<String>)msg.getAttachmentNames()) {
            final TH01WaferMap map = createWafermap(msg.getAttachment(name));
            final int passCnt = map.getPassCnt();
        }

        msg.setContent(new StringSource(format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<event>\n" +
                                       "<type>number_of_dies</type>\n" +
                                       "<lotname>A84001</lotname>" +
                                       "<item>201234500</item>" +
                                       "<when>20110207-16:02</when>" +
                                       "<operator>brh</operator>" +
                                       "<probing-site>IEPER</probing-site>" +
                                       "<wafers>" +
                                       "<wafer id=\"1\" passdies=\"%d\" />" +
                                       "<wafer id=\"2\" passdies=\"%d\" />" +
                                       "</wafers>" +
                                               "</event>", 55, 88)));

    }

    private static TH01WaferMap createWafermap(final DataHandler handler) throws IOException {
        final DataSource ds = handler.getDataSource();
        return new TH01WaferMap(toByteArray(ds.getInputStream()));
    }

    public void setDeliveryChannel(final DeliveryChannel deliveryChannel) {
        this.channel = deliveryChannel;
    }
}
