package com.melexis;

import org.junit.*;

import static org.junit.Assert.*;

public class LotTest {

    private Lot lot;

    private Wafer wafer1;
    private Wafer wafer2;

    @Before public void before() {
        lot = new Lot();

        wafer1 = new Wafer(1);
        wafer2 = new Wafer(2);

        lot.addWafer(wafer1);
        lot.addWafer(wafer2);
    }

    @Test public void deviceTest() {
        lot.setItem("201210600");

        assertEquals("12106", lot.getDevice());
    }

    @Test(expected=IllegalArgumentException.class) public void nullItem() {
        lot.getDevice();
    }

    @Test(expected=IllegalArgumentException.class) public void shortItem() {
        lot.setItem("121");

        lot.getDevice();
    }

    @Test public void addWafer() {
        final Wafer w = new Wafer(3);
        lot.addWafer(w);

        assertTrue(lot.getWafers().contains(w));
    }

    @Test public void findWaferWithNumber() {
        assertEquals(wafer2, lot.findWaferWithNumber(2));
    }

    @Test(expected=IllegalArgumentException.class) public void noWaferWithNumber() {
        lot.findWaferWithNumber(100);
    }
}