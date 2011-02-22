package com.melexis.util;

import com.melexis.Lot;
import com.melexis.Wafer;
import com.melexis.exception.InvalidModelException;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.util.ByteArrayDataSource;

import javax.activation.DataHandler;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.io.IOUtils.toByteArray;

public class MessageUtil {

    private XmlUtil xmlUtil;

    public MessageUtil(final XmlUtil xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    public Lot fromMessage(NormalizedMessage msg) {
        final StringSource content = (StringSource) msg.getContent();
        final String xml = content.getText();

        try {
            final Lot l = xmlUtil.toLot(xml);

            for (final Wafer w : l.getWafers()) {
                final Map<String,byte[]> wafermaps = w.getWafermaps();
                for (final String filename : wafermaps.keySet()) {
                    final byte[] buff = attachmentToBuffer(msg, filename);
                    wafermaps.put(filename, buff);
                }
            }

            return l;
        } catch(Exception e) {
            throw new InvalidModelException(xml, e);
        }
    }

    public void toMessage(final NormalizedMessage m, final Lot l) throws MessagingException {
        final String xml = xmlUtil.toXml(l);
        m.setContent(new StringSource(xml));

        for (final Wafer wafer : l.getWafers()) {
            final Map<String, byte[]> wafermaps = wafer.getWafermaps();
            for (final String filename : wafermaps.keySet()) {
                m.addAttachment(filename, new DataHandler(new ByteArrayDataSource(wafermaps.get(filename), "bin/th01")));
            }
        }
    }

    public void withMessage(final NormalizedMessage m, final LotTransformer transformer) throws MessagingException {
        final Lot l = fromMessage(m);
        final Lot processed = transformer.process(l);
        toMessage(m, processed);
    }

    private byte[] attachmentToBuffer(NormalizedMessage msg, String filename) throws IOException {
        final DataHandler handler = msg.getAttachment(filename);        
        return toByteArray(handler.getDataSource().getInputStream());
    }
}
