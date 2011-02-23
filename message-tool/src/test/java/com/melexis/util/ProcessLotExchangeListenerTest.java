package com.melexis.util;

import com.melexis.Lot;
import com.melexis.exception.TransformException;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.junit.Before;
import org.junit.Test;

import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.NormalizedMessage;

import static org.mockito.Mockito.*;

public class ProcessLotExchangeListenerTest {

    private ProcessLotExchangeListener listener;

    private DeliveryChannel channel;
    private MessageUtil util;
    private LotTransformer transformer;
    
    @Before
    public void setUp() {
        util = new MessageUtil(new XmlUtil());
        transformer = mock(LotTransformer.class);
        channel = mock(DeliveryChannel.class);

        listener = new ProcessLotExchangeListener(util, transformer);
        listener.setChannel(channel);
    }

    @Test public void process() throws Exception {
        final InOut inOut = mock(InOut.class);
        final NormalizedMessage msg = mock(NormalizedMessage.class);

        // expectations
        when(inOut.getStatus()).thenReturn(ExchangeStatus.ACTIVE);
        when(inOut.getInMessage()).thenReturn(msg);

        when(msg.getContent()).thenReturn(new StringSource("<lot />"));
        when(transformer.process(isA(Lot.class))).thenReturn(new Lot());

        //  for transfering in to out.
        when(inOut.getMessage("in")).thenReturn(msg);
        when(inOut.createMessage()).thenReturn(msg);

        // process
        listener.onMessageExchange(inOut);

        // validations
        verify(transformer).process(isA(Lot.class));
        verify(channel).send(inOut);
    }

    @Test(expected = TransformException.class) public void processException() throws Exception {
                final InOut inOut = mock(InOut.class);
        final NormalizedMessage msg = mock(NormalizedMessage.class);

        // expectations
        when(inOut.getStatus()).thenReturn(ExchangeStatus.ACTIVE);
        when(inOut.getInMessage()).thenReturn(msg);

        when(transformer.process(isA(Lot.class))).thenThrow(new Exception());

        // process
        listener.onMessageExchange(inOut);
    }
}
