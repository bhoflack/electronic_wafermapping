package com.melexis.util;

import com.melexis.exception.TransformException;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;

import javax.annotation.Resource;
import javax.jbi.messaging.*;

import static org.apache.servicemix.jbi.util.MessageUtil.transferInToOut;

/**
 * Message exchange listener that assumes a lot event is sent in and a lot event is sent out.
 *
 * The lotTransformer contains the meat of the message exchange listener. 
 */
public class ProcessLotExchangeListener implements MessageExchangeListener {

    private final MessageUtil util;
    private final LotTransformer lotTransformer;

    @Resource private DeliveryChannel channel;

    public ProcessLotExchangeListener(final MessageUtil util, final LotTransformer lotTransformer) {
        this.util = util;
        this.lotTransformer = lotTransformer;
    }

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        final InOut inOut = (InOut) exchange;

        if (inOut.getStatus() == ExchangeStatus.ACTIVE) {
            final NormalizedMessage msg = inOut.getInMessage();

            try {
                util.withMessage(msg, lotTransformer);
            } catch (Exception e) {
                throw new TransformException(e);
            }

            transferInToOut(inOut, inOut);
            channel.send(inOut);
        }
    }

    public void setChannel(DeliveryChannel channel) {
        this.channel = channel;
    }
}
