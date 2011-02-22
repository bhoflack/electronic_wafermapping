package com.melexis;

import com.melexis.util.XmlUtil;
import com.melexis.wafermaps.WaferService;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataHandler;
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
    private XmlUtil xmlUtil;

    @Before public void setUp() {
        waferService = mock(WaferService.class);
        xmlUtil = new XmlUtil();

        wmdb = new WMDBBean(waferService, xmlUtil);
    }

   @Test public void findWafermapsForLot() throws Exception {
       final String lotname = "A12345";

       final InOut inOut = mock(InOut.class);
       final NormalizedMessage msg = mock(NormalizedMessage.class);
       final StringSource contents = new StringSource(createLotModel());


       // expectations
       when(inOut.getStatus()).thenReturn(ExchangeStatus.ACTIVE);
       when(inOut.getInMessage()).thenReturn(msg);
       when(msg.getContent()).thenReturn(contents);

       //  get the wafermaps
       for (int i=1; i<=4; i++) {
           when(waferService.findWafermapsByLotnameAndWaferid(lotname, i))
               .thenReturn(foundWafers(lotname, i));
       }

        //  for transfering in to out.
        when(inOut.getMessage("in")).thenReturn(msg);
        when(inOut.createMessage()).thenReturn(msg);

       

       // perform the processor
       wmdb.onMessageExchange(inOut);

       // verify the wafermaps are attached to the message
       for (int i=1; i<=4; i++) {
           verify(msg).addAttachment(eq(format("%s-%d.th01", lotname, i)), isA(DataHandler.class));
           verify(msg).addAttachment(eq(format("%s-%d-2.th01", lotname, i)), isA(DataHandler.class));
       }

    }

    private static Map<String, byte[]> foundWafers(final String lotname, final int wafer) {
        final Map<String, byte[]> wafers = new HashMap<String, byte[]>();

        wafers.put(format("%s-%d.th01", lotname, wafer), "wafermap1".getBytes());
        wafers.put(format("%s-%d-2.th01", lotname, wafer), "wafermap2".getBytes());
        return wafers;
    }

    private static String createLotModel() {
        return "<lot name=\"A12345\" item=\"201210600\" organization=\"MLX_IEP_OPS_IO\" probelocation=\"MLX_IEP_OPS_IO\" subcontractor=\"O_AMKOR_1\">\n" +
            "  <wafer number=\"1\"/>\n" +
            "  <wafer number=\"2\"/>\n" +
            "  <wafer number=\"3\"/>\n" +
            "  <wafer number=\"4\"/>\n" +
            "</lot>";
    }
}