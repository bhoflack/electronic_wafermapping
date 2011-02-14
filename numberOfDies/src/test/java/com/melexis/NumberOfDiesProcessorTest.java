package com.melexis;

import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.util.jaf.ByteArrayDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataHandler;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.NormalizedMessage;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.mockito.Mockito.*;

public class NumberOfDiesProcessorTest {

    private NumberOfDiesProcessor nod;
    private DeliveryChannel deliveryChannel;

    @Before public void setUp() {
        deliveryChannel = mock(DeliveryChannel.class);
        nod = new NumberOfDiesProcessor();

        nod.setDeliveryChannel(deliveryChannel);
    }

    @Test public void extractNumberOfDies() throws Exception {
        final InOut inOut = mock(InOut.class);
        final NormalizedMessage msg = mock(NormalizedMessage.class);
        final StringSource contents = inMessage();


        // expectations
        when(inOut.getStatus()).thenReturn(ExchangeStatus.ACTIVE);
        when(inOut.getInMessage()).thenReturn(msg);
        when(msg.getContent()).thenReturn(contents);
        when(msg.getAttachment("A84001-1.th01")).thenReturn(datahandlerForFile("A84001-1.th01"));
        when(msg.getAttachment("A84001-2.th01")).thenReturn(datahandlerForFile("A84001-2.th01"));

        // for transfering in to out.
        when(inOut.getMessage("in")).thenReturn(msg);
        when(inOut.createMessage()).thenReturn(msg);

        // perform the processor
        nod.onMessageExchange(inOut);

        // verifications
        verify(msg, times(2)).setContent(isA(StringSource.class));
        verify(deliveryChannel).send(inOut);
    }

    private static DataHandler datahandlerForFile(final String filename) throws IOException {
        final byte[] wafermap = toByteArray(NumberOfDiesProcessorTest.class.getClassLoader().getResourceAsStream(filename));

        return new DataHandler(new ByteArrayDataSource(wafermap, "bin/th01"));
    }

    private static StringSource inMessage() {
        return new StringSource("<?xml version=\"1.0\" encoding=\"UTF-8\"?><event>" +
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
                                "</event>");
    }
}