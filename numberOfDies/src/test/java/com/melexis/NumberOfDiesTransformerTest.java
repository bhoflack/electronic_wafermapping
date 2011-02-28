package com.melexis;

import java.util.Map;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.junit.Assert.*;

public class NumberOfDiesTransformerTest {

    private NumberOfDiesTransformer transformer;
    private byte[] WMAP;

    @Before public void setUp() throws Exception {
        transformer = new NumberOfDiesTransformer();
        WMAP = toByteArray(NumberOfDiesTransformerTest.class.getClassLoader()
                           .getResourceAsStream("A84001-1.th01"));
    }

    @Test public void extractNumberOfPassDies() {
        final Lot l = createLot();

        final Lot processed = transformer.process(l);
        assertEquals(Integer.valueOf(889), processed.findWaferWithNumber(1).getPassdies());
    }

    private final Lot createLot() {
        final Lot l = new Lot();
        for (int i=1; i<=5; i++) {
            final Wafer w = new Wafer(i);
            final Map<String, byte[]> wafermaps = new HashMap<String, byte[]>();
            wafermaps.put("blaat" + i, WMAP);

            w.setWafermaps(wafermaps);
            l.addWafer(w);
        }
        return l;
    }
}