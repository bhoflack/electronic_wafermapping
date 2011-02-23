package com.melexis;

import org.junit.*;

import static org.junit.Assert.*;

public class LotTest {

    @Test public void deviceTest() {
        final Lot lot = new Lot();
        lot.setItem("201210600");

        assertEquals("12106", lot.getDevice());
    }

    @Test(expected=IllegalArgumentException.class) public void nullItem() {
        final Lot lot = new Lot();

        lot.getDevice();
    }

    @Test(expected=IllegalArgumentException.class) public void shortItem() {
        final Lot lot = new Lot();
        lot.setItem("121");

        lot.getDevice();
    }
}