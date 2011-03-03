package com.melexis.util;

import com.melexis.Lot;
import com.melexis.Wafer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.Collections;
import java.util.Set;

import static java.lang.String.format;

public class XmlUtil {

    public Lot toLot(final String xml) throws SAXException, IOException {
        final XMLReader xr = XMLReaderFactory.createXMLReader();
	    final LotHandler handler = new LotHandler();
	    xr.setContentHandler(handler);
	    xr.setErrorHandler(handler);
        xr.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
        return handler.getLot();
    }

    public String toXml(final Lot lot) {
        final StringBuffer sb = new StringBuffer();
        sb.append("<lot");
        appendIfNotNull(sb, " name=\"%s\"", lot.getName());
        appendIfNotNull(sb, " item=\"%s\"", lot.getItem());
        appendIfNotNull(sb, " organization=\"%s\"", lot.getOrganization());
        appendIfNotNull(sb, " probelocation=\"%s\"", lot.getProbelocation());
        appendIfNotNull(sb, " subcontractor=\"%s\"", lot.getSubcontractor());
        sb.append(">\n");

        if (!lot.getConfig().equals(Collections.emptyMap())) {
            sb.append("  <configuration-parameters>\n");
            for (final String k : lot.getConfig().keySet()) {
                sb.append(format("    <parameter key=\"%s\" value=\"%s\" />\n", k, lot.getConfig().get(k)));
            }
            sb.append("  </configuration-parameters>\n");
        }

        for (final Wafer wafer : lot.getWafers()) {
            sb.append(toXml(wafer));
        }

        sb.append("</lot>");
        return sb.toString();
    }

    public String toXml(final Wafer wafer) {
        final StringBuffer sb = new StringBuffer();
        sb.append("  <wafer");
        appendIfNotNull(sb, " number=\"%s\"", wafer.getWafernumber());
        appendIfNotNull(sb, " passdies=\"%s\"", wafer.getPassdies());
        sb.append(">\n");

        if (wafer.getValidationmessages().size() > 0) {
            sb.append("    <validation-messages>\n");
            for (final String m : wafer.getValidationmessages()) {
                sb.append(format("      <message value=\"%s\" />\n", m));
            }
            sb.append("    </validation-messages>\n");
        }

        for (final String filename : wafer.getWafermaps().keySet()) {
            sb.append(toXml(filename));
        }
        sb.append("  </wafer>\n");
        return sb.toString();
    }

    private static void appendIfNotNull(final StringBuffer sb, final String pattern, final Object value) {
        if (value != null) {
            sb.append(format(pattern, value));
        }
    }

    private final String toXml(final String filename) {
        return format("    <wafermap name=\"%s\" />\n", filename);
    }
}
