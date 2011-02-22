package com.melexis;

import com.melexis.util.LotTransformer;
import com.melexis.util.MessageUtil;
import com.melexis.wafermaps.WaferService;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;

import javax.annotation.Resource;
import javax.jbi.messaging.*;
import java.util.Map;

import static org.apache.servicemix.jbi.helper.MessageUtil.transferInToOut;

public class WMDBBean implements MessageExchangeListener {

    private final WaferService waferService;
    private final MessageUtil messageUtil;

    @Resource
    private DeliveryChannel channel;

    public WMDBBean(final WaferService waferService, final MessageUtil messageUtil) {
        this.waferService = waferService;
        this.messageUtil = messageUtil;
    }

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        final InOut inOut = (InOut) exchange;

        if (inOut.getStatus() == ExchangeStatus.ACTIVE) {
            final NormalizedMessage msg = inOut.getInMessage();

            messageUtil.withMessage(msg, new LotTransformer() {
                public Lot process(Lot lot) {
                    for (final Wafer w : lot.getWafers()) {
                        final Map<String, byte[]> maps = waferService.findWafermapsByLotnameAndWaferid(lot.getName(), w.getWafernumber());
                        w.setWafermaps(maps);
                    }

                    return lot;
                }
            });
            
            transferInToOut(inOut, inOut);
            channel.send(inOut);
        }
    }

    public void setChannel(DeliveryChannel channel) {
        this.channel = channel;
    }
}
