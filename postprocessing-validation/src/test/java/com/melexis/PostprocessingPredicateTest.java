package com.melexis;

import com.melexis.th01.TH01WaferMap;

import org.junit.Before;
import org.junit.Test;

import static com.melexis.util.IOUtils.resource;
import static org.junit.Assert.*;

public class PostprocessingPredicateTest {

    private PostprocessingPredicate predicate;

    private Lot l;
    private Wafer w;

    @Before public void setUp() {
        predicate = new PostprocessingPredicate();

        l = new Lot();
        w = new Wafer(1);
        l.addWafer(w);
    }

    @Test public void pass() throws Exception {
        final TH01WaferMap withTh01 = new TH01WaferMap(resource("had_postprocessing.th01"));

        assertTrue(predicate.apply(l, w, withTh01));
    }

    @Test public void noPostprocessing() throws Exception {
        final TH01WaferMap without = new TH01WaferMap(resource("no_postprocessing.th01"));

        assertFalse(predicate.apply(l, w, without));
    }
}