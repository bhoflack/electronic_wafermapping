package com.melexis;

import com.melexis.th01.TH01WaferMap;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.junit.Assert.assertEquals;

public class PassdiePredicateTest {

    private PassdiePredicate predicate;

    @Before public void setUp() {
        predicate = new PassdiePredicate();
    }

    @Test public void valid() throws Exception {
        verifyValid("example.th01", 472, true);
    }

    @Test public void invalid() throws Exception {
        verifyValid("example.th01", 66, false);
    }

    private void verifyValid(final String location, int passdies, boolean expected) throws Exception {
        final Lot l = new Lot();
        final Wafer w = new Wafer(1);
        w.setPassdies(passdies);
        final TH01WaferMap wmap = new TH01WaferMap(resource(location));

        assertEquals(expected, predicate.apply(l, w, wmap));
    }

    private final static byte[] resource(final String filename) throws Exception {
        return toByteArray(PassdiePredicateTest.class.getClassLoader().getResourceAsStream(filename));
    }
}
