package com.melexis;

import com.melexis.util.MessageUtil;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;

import javax.annotation.Resource;
import javax.jbi.messaging.*;

import static org.apache.servicemix.jbi.helper.MessageUtil.transferInToOut;

public class WMDBBean implements MessageExchangeListener {

    private final AttachWafermapsTransformer attachWafermapTransformer;
    private final MessageUtil messageUtil;

    @Resource
    private DeliveryChannel channel;

    public WMDBBean(final AttachWafermapsTransformer attachWafermapTransformer, final MessageUtil messageUtil) {
        this.attachWafermapTransformer = attachWafermapTransformer;
        this.messageUtil = messageUtil;
    }

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        final InOut inOut = (InOut) exchange;

        if (inOut.getStatus() == ExchangeStatus.ACTIVE) {
            final NormalizedMessage msg = inOut.getInMessage();

            messageUtil.withMessage(msg, attachWafermapTransformer);

            transferInToOut(inOut, inOut);
            channel.send(inOut);
        }
    }

    public void setChannel(DeliveryChannel channel) {
        this.channel = channel;
    }
}
