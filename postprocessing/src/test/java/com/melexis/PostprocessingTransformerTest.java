package com.melexis;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.ImmutableSet.of;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PostprocessingTransformerTest {

    private final static String URL = "http://localhost/postprocess.cgi";

    private PostprocessingTransformer postprocessing;
    private HttpClient client;

    @Before public void before() {
        client = mock(HttpClient.class);

        postprocessing = new PostprocessingTransformer(URL, client);
    }

    @Test public void process() throws Exception {
        final String response = new String(toByteArray(getClass().getClassLoader().getResourceAsStream("postprocessing.yaml")));
        final byte[] th01map = toByteArray(getClass().getClassLoader().getResourceAsStream("example.th01"));

        // expectations
        when(client.get(URL + "?lotname=ABC123")).thenReturn(response);

        // the request
        final Lot l = new Lot();
        l.setName("ABC123");
        l.setWafers(of(new Wafer(1), new Wafer(2)));
        final Lot p = postprocessing.process(l);

        // verifications
        assertEquals(2, p.getWafers().size());

        final Wafer first = p.findWaferWithNumber(1);
        assertTrue(first.getWafermaps().containsKey("postprocessing"));
        assertArrayEquals(th01map, first.getWafermaps().get("postprocessing"));
    }
}