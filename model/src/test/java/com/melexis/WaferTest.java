package com.melexis;

import org.junit.*;

import static org.junit.Assert.*;

public class WaferTest {

    @Test public void addValidationmessage() {
        final Wafer w = new Wafer();
        final String message = "This is a validation message.";

        w.addValidationmessage(message);
        assertTrue(w.getValidationmessages().contains(message));
    }
}