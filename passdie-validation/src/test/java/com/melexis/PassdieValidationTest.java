package com.melexis;

import java.util.Collection;

import org.junit.*;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.format;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.junit.Assert.*;

public class PassdieValidationTest {

    private PassdieValidationTransformer passdie;

    @Before public void setUp() {
        passdie = new PassdieValidationTransformer();
    }

    @Test public void validation() throws Exception {
        final String error_template = "Wafermap %s contains a wrong number of dies.";

        final Wafer first = new Wafer(1);
        final Wafer second = new Wafer(2);
        first.setPassdies(472);
        second.setPassdies(23);

        final byte[] example = resource("example.th01");
        final byte[] other = resource("other.th01");

        first.setWafermaps(of("valid", example, "invalid", other));
        second.setWafermaps(of("invalid", example, "invalid2", other));

        final Lot l = new Lot();
        l.addWafer(first);
        l.addWafer(second);

        final Lot p = passdie.process(l);

        final Wafer pw1 = p.findWaferWithNumber(1);
        final Wafer pw2 = p.findWaferWithNumber(2);

        assertEquals(1, pw1.getWafermaps().size());
        assertEquals(0, pw2.getWafermaps().size());

        assertContains(format(error_template, "invalid"), pw1.getValidationmessages());
        assertContains(format(error_template, "invalid"), pw2.getValidationmessages());
        assertContains(format(error_template, "invalid2"), pw2.getValidationmessages());
    }

    private final static byte[] resource(final String filename) throws Exception {
        return toByteArray(PassdieValidationTest.class.getClassLoader().getResourceAsStream(filename));
    }

    private final static void assertContains(final String expected, final Collection<String> collection) {
        assertTrue("Collection does not contain item " + expected, collection.contains(expected));
    }

}