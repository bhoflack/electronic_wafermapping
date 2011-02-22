package com.melexis;

import com.melexis.util.MessageUtil;
import com.melexis.wafermaps.WaferService;
import org.junit.Before;
import org.junit.Test;

import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.NormalizedMessage;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.mockito.Mockito.*;

public class WMDBBeanTest {

    private WMDBBean wmdb;
    private WaferService waferService;
    private MessageUtil messageUtil;
    private DeliveryChannel channel;

    @Before public void setUp() {
        waferService = mock(WaferService.class);
        messageUtil = mock(MessageUtil.class);
        channel = mock(DeliveryChannel.class);

        wmdb = new WMDBBean(waferService, messageUtil);

        wmdb.setChannel(channel);
    }

   @Test public void findWafermapsForLot() throws Exception {
       final String lotname = "A12345";

       // expectations
       final InOut inOut = mock(InOut.class);
       final NormalizedMessage msg = mock(NormalizedMessage.class);

       final Lot lot = createLotModel();
       when(inOut.getStatus()).thenReturn(ExchangeStatus.ACTIVE);
       when(inOut.getInMessage()).thenReturn(msg);
       
       //  get the wafermaps
       for (final Wafer wafer : lot.getWafers()) {
           wafer.setWafermaps(foundWafers(lotname, wafer.getWafernumber()));           
       }

        //  for transfering in to out.
        when(inOut.getMessage("in")).thenReturn(msg);
        when(inOut.createMessage()).thenReturn(msg);

       // perform the processor
       wmdb.onMessageExchange(inOut);

       verify(channel).send(inOut);
    }

    private static Map<String, byte[]> foundWafers(final String lotname, final int wafer) {
        final Map<String, byte[]> wafers = new HashMap<String, byte[]>();

        wafers.put(format("%s-%d.th01", lotname, wafer), "wafermap1".getBytes());
        wafers.put(format("%s-%d-2.th01", lotname, wafer), "wafermap2".getBytes());
        return wafers;
    }

    private static Lot createLotModel() {
        final Lot l = new Lot();
        l.setName("A12345");
        l.setItem("201210600");
        l.setOrganization("MLX_IEP_OPS_IO");
        l.setProbelocation("MLX_IEP_OPS_IO");
        l.setSubcontractor("O_AMKOR_1");

        for (int i=1; i<=4; i++) {
            final Wafer w = new Wafer(i);
            l.getWafers().add(w);
        }

        return l;
    }
}