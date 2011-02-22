package com.melexis;

import com.melexis.exception.InvalidModelException;
import com.melexis.util.XmlUtil;
import com.melexis.wafermaps.WaferService;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.listener.MessageExchangeListener;
import org.apache.servicemix.util.jaf.ByteArrayDataSource;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.jbi.messaging.*;
import java.util.Map;

public class WMDBBean implements MessageExchangeListener {

    private final WaferService waferService;
    private final XmlUtil xmlUtil;

    @Resource private DeliveryChannel channel;

    public WMDBBean(final WaferService waferService, final XmlUtil xmlUtil) {
        this.waferService = waferService;
        this.xmlUtil = xmlUtil;
    }

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        final InOut inOut = (InOut) exchange;

        if (inOut.getStatus() == ExchangeStatus.ACTIVE) {
            final NormalizedMessage msg = inOut.getInMessage();
            final StringSource contents = (StringSource) msg.getContent();
            final String c = contents.getText();

            try {
                
                final Lot lot = xmlUtil.toLot(c);

                for (final Wafer w : lot.getWafers()) {
                    final Map<String, byte[]> maps = waferService.findWafermapsByLotnameAndWaferid(lot.getName(), w.getWafernumber());

                    for (final String key: maps.keySet()) {
                        msg.addAttachment(key, new DataHandler(new ByteArrayDataSource(maps.get(key), "bin/th01")));
                    }
                }

                

            } catch (Exception e) {
                throw new InvalidModelException(c, e);
            }
        }
    }
}
