package com.melexis;

import com.melexis.th01.TH01WaferMap;

import org.junit.Before;
import org.junit.Test;

import static com.melexis.util.IOUtils.resource;
import static org.junit.Assert.*;

public class RowColumnPredicateTest {

    private RowColumnPredicate predicate;

    private Lot l;
    private Wafer w;

    @Before public void setUp() {
        predicate = new RowColumnPredicate();

        l = new Lot();
        w = new Wafer(1);
        l.addWafer(w);
        l.getConfig().put("ROWS", "32");
        l.getConfig().put("COLS", "28");
    }

    @Test public void pass() throws Exception {
        final TH01WaferMap withTh01 = new TH01WaferMap(resource("sample.th01"));
        assertTrue(predicate.apply(l, w, withTh01));
    }


}