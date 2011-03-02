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
    private TH01WaferMap th01;

    @Before public void setUp() throws Exception {
        predicate = new RowColumnPredicate();

        l = new Lot();
        w = new Wafer(1);
        l.addWafer(w);
        l.getConfig().put("ROWS", "32");
        l.getConfig().put("COLS", "28");

        th01 = new TH01WaferMap(resource("sample.th01"));
    }

    @Test public void pass() {
        assertTrue(predicate.apply(l, w, th01));
    }

    @Test public void failRows() {
        l.getConfig().put("ROWS", "31");
        assertFalse(predicate.apply(l, w, th01));
    }

    @Test public void failCols() {
        l.getConfig().put("COLS", "29");
        assertFalse(predicate.apply(l, w, th01));
    }
}