package com.melexis.util;

import com.melexis.Lot;
import com.melexis.Wafer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LotHandler extends DefaultHandler {

    private final static String LOT = "lot";
    private final static String WAFER = "wafer";
    private final static String WAFERMAP = "wafermap";

    private boolean inLot;
    private boolean inWafer;
    private boolean inWafermap;

    private Lot lot;
    private Wafer wafer;
    private String filename;

    @Override
    public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
        final String element = element(uri, name, qName);

        if (LOT.equals(element)) {
            inLot = true;
            lot = new Lot();

            lot.setName(attributes.getValue("name"));
            lot.setItem(attributes.getValue("item"));
            lot.setOrganization(attributes.getValue("organization"));
            lot.setProbelocation(attributes.getValue("probelocation"));
            lot.setSubcontractor(attributes.getValue("subcontractor"));

        } else if (WAFER.equals(element) && inLot) {
            inWafer = true;

            wafer = new Wafer();
            wafer.setWafernumber(intValue("number", attributes));
            wafer.setPassdies(intValue("passdies", attributes));
        } else if (WAFERMAP.equals(element)) {
            inWafermap = true;

            filename = attributes.getValue("name");
            wafer.getWafermaps().put(filename, null);
        }

    }

    @Override
    public void endElement(String uri, String name, String qName) throws SAXException {
        if (inWafer) {
            lot.getWafers().add(wafer);
            inWafer = false;
        }
    }

    public Lot getLot() {
        return lot;
    }

    private static Integer intValue(final String name, final Attributes attributes) {
        final String v = attributes.getValue(name);

        if (v == null) {
            return null;
        } else {
            return Integer.valueOf(v);
        }
    }

    private static String element(final String uri, String name, String qName) {
        if ("".equals(uri)) {
            return qName;
        }

        return name;
    }
}
