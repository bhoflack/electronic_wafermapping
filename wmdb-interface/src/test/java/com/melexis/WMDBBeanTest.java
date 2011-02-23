package com.melexis;

import com.melexis.util.MessageUtil;
import org.junit.Before;
import org.junit.Test;

import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.NormalizedMessage;

import static org.mockito.Mockito.*;

public class WMDBBeanTest {

    private WMDBBean wmdb;
    private AttachWafermapsTransformer attachWafermapsTransformer;
    private MessageUtil messageUtil;
    private DeliveryChannel channel;

    @Before public void setUp() {
        attachWafermapsTransformer = mock(AttachWafermapsTransformer.class);
        messageUtil = mock(MessageUtil.class);
        channel = mock(DeliveryChannel.class);

        wmdb = new WMDBBean(attachWafermapsTransformer, messageUtil);

        wmdb.setChannel(channel);
    }

   @Test public void findWafermapsForLot() throws Exception {
       // expectations
       final InOut inOut = mock(InOut.class);
       final NormalizedMessage msg = mock(NormalizedMessage.class);

       when(inOut.getStatus()).thenReturn(ExchangeStatus.ACTIVE);
       when(inOut.getInMessage()).thenReturn(msg);

        //  for transfering in to out.
        when(inOut.getMessage("in")).thenReturn(msg);
        when(inOut.createMessage()).thenReturn(msg);

       // perform the processor
       wmdb.onMessageExchange(inOut);
       
       verify(channel).send(inOut);
    }
}