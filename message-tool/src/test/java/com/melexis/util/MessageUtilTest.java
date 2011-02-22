package com.melexis.util;

import com.melexis.Lot;
import com.melexis.Wafer;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.util.ByteArrayDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataHandler;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageUtilTest {

    private MessageUtil messageUtil;
    private XmlUtil xmlUtil;
    private byte[] wafermap1;
    private byte[] wafermap2;

    @Before public void setUp() {
        xmlUtil = mock(XmlUtil.class);
        messageUtil = new MessageUtil(xmlUtil);
        wafermap1 = "ik ben een wafermap".getBytes();
        wafermap2 = "ik ook".getBytes();
    }

    @Test public void fromMesssage() throws Exception {
        final NormalizedMessage msg = mock(NormalizedMessage.class);
        final String xml = "<lot />";

        final Lot l = createLot();

        when(msg.getContent()).thenReturn(new StringSource(xml));
        when(xmlUtil.toLot(xml)).thenReturn(l);

        when(msg.getAttachment("blaat")).thenReturn(new DataHandler(new ByteArrayDataSource(wafermap1, "bin/th01")));
        when(msg.getAttachment("blub")).thenReturn(new DataHandler(new ByteArrayDataSource(wafermap2, "bin/th01")));

        final Lot lot = messageUtil.fromMessage(msg);
        final Wafer wafer = lot.getWafers().iterator().next();
        
        assertArrayEquals(wafermap1, wafer.getWafermaps().get("blaat"));
        assertArrayEquals(wafermap2, wafer.getWafermaps().get("blub"));        
    }
    
    @Test public void toMessage() throws MessagingException {
        final String xml = "<lot />";
        final Lot l = createLot(wafermap1, wafermap2);
        final NormalizedMessage m = mock(NormalizedMessage.class);

        // Translate the lot to xml.
        when(xmlUtil.toXml(l)).thenReturn(xml);

        messageUtil.toMessage(m, l);

        verify(m).setContent(isA(StringSource.class));
        verify(m).addAttachment(eq("blaat"), isA(DataHandler.class));
        verify(m).addAttachment(eq("blub"), isA(DataHandler.class));
    }

    private static Lot createLot() {
        return createLot(null, null);
    }

    private static Lot createLot(final byte[] wafermap1, final byte[] wafermap2) {
        final Lot l = new Lot();
        final Wafer w = new Wafer();

        final Map<String, byte[]> wafermaps = new HashMap<String, byte[]>();
        wafermaps.put("blaat", wafermap1);
        wafermaps.put("blub", wafermap2);
        w.setWafermaps(wafermaps);
        l.getWafers().add(w);
        return l;
    }

}
