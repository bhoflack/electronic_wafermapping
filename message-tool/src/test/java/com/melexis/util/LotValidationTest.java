package com.melexis.util;

import com.google.common.collect.ImmutableSet;
import com.melexis.Lot;
import com.melexis.Wafer;
import com.melexis.th01.TH01WaferMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LotValidationTest {

    private LotValidation lv;

    private String t;
    private LotPredicate p;

    @Before public void setUp() {
        p = mock(LotPredicate.class);
        t = "Error blaat %s";

        lv = new LotValidation(p, t);
    }

    @Test public void process() throws Exception {
        final TH01WaferMap first = new TH01WaferMap(resource("example.th01"));
        final TH01WaferMap other = new TH01WaferMap(resource("other.th01"));

        final Wafer w1 = new Wafer(1);
        w1.setWafermaps(of("first", first.toBytes()));

        final Wafer w2 = new Wafer(2);
        w2.setWafermaps(of("first", first.toBytes(), "other", other.toBytes()));

        final Lot l = new Lot();
        l.addWafer(w1);
        l.addWafer(w2);

        when(p.apply(eq(l), eq(w1), isA(TH01WaferMap.class))).thenReturn(true);
        when(p.apply(eq(l), eq(w2), isA(TH01WaferMap.class))).thenReturn(false);

        final Lot pr = lv.process(l);
        final Wafer pw1 = pr.findWaferWithNumber(1);
        final Wafer pw2 = pr.findWaferWithNumber(2);


        assertEquals(1, pw1.getWafermaps().size());
        assertTrue(pw1.getWafermaps().containsKey("first"));
        assertEquals(0, pw2.getWafermaps().size());

        // verify the validation messages are added.
        assertEquals(Collections.emptySet(), pw1.getValidationmessages());
        assertEquals(ImmutableSet.of("Error blaat first", "Error blaat other"), pw2.getValidationmessages());
    }

    private final static byte[] resource(final String filename) throws Exception {
        return toByteArray(LotValidationTest.class.getClassLoader().getResourceAsStream(filename));
    }
}