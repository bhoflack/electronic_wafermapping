package com.melexis;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigTransformerTest {

    private final static Map<String, String> NO_CONFIG = Collections.emptyMap();

    private ConfigTransformer config;
    private ConfigDao dao;

    @Before public void setUp() {
        dao = mock(ConfigDao.class);
        config = new ConfigTransformer(dao);
    }

    @Test public void process() throws Exception {
        final Lot l = new Lot("A12345", "201210600", "MLX_IEP_OPS_IO", "MLX_EXT_IEP_IO", "O_AMKOR_1");

        // expectations
        //  the name is the most detailed and should be taken
        when(dao.findForKey("name", l.getName()))
            .thenReturn(of("ROWS", "30", "COLS", "40"));
        //  the item also has a configuration,  but should be ignored
        when(dao.findForKey("item", l.getItem()))
            .thenReturn(of("ROWS", "50", "COLS", "60"));
        //  other parameters have no configuration
        when(dao.findForKey("device", l.getDevice()))
            .thenReturn(NO_CONFIG);
        when(dao.findForKey("organization", l.getOrganization()))
            .thenReturn(NO_CONFIG);
        when(dao.findForKey("subcontractor", l.getSubcontractor()))
            .thenReturn(NO_CONFIG);

        //   the probelocation has the searchservice
        when(dao.findForKey("probelocation", l.getProbelocation()))
             .thenReturn(of("searchservice", "sda.sensors.elex.be"));

        // process the lot
        final Lot processed = config.process(l);

        // validate that the properties were set
        assertEquals("30", processed.getConfig().get("ROWS"));
        assertEquals("40", processed.getConfig().get("COLS"));

        assertEquals("sda.sensors.elex.be", processed.getConfig().get("searchservice"));
    }

}