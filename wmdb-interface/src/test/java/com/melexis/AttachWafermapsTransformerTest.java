package com.melexis;

import com.melexis.wafermaps.WaferService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttachWafermapsTransformerTest {

    private AttachWafermapsTransformer attachWafermapsTransformer;
    private WaferService waferService;

    @Before public void setUp() {
        waferService = mock(WaferService.class);
        attachWafermapsTransformer = new AttachWafermapsTransformer(waferService);
    }

    @Test public void process() {
        final Lot l = createLotModel();

        // expectations
        for (int i=1; i<4; i++) {
            when(waferService.findWafermapsByLotnameAndWaferid(l.getName(), i))
                .thenReturn(foundWafers(l.getName(), i));
        }

        // perform the process
        final Lot processed = attachWafermapsTransformer.process(l);

        // verifications
        for (final Wafer wafer : processed.getWafers()) {
            final Map<String, byte[]> wafermaps = foundWafers(processed.getName(), wafer.getWafernumber());

            for (final String file : wafer.getWafermaps().keySet()) {
                assertArrayEquals(wafermaps.get(file), wafer.getWafermaps().get(file));
            }
        }
    }

    private static Map<String, byte[]> foundWafers(final String lotname, final int wafer) {
        final Map<String, byte[]> wafers = new HashMap<String, byte[]>();

        wafers.put(format("%s-%d.th01", lotname, wafer), "wafermap1".getBytes());
        wafers.put(format("%s-%d-2.th01", lotname, wafer), "wafermap2".getBytes());
        return wafers;
    }

    private static Lot createLotModel() {
        final Lot l = new Lot();
        l.setName("A12345");
        l.setItem("201210600");
        l.setOrganization("MLX_IEP_OPS_IO");
        l.setProbelocation("MLX_IEP_OPS_IO");
        l.setSubcontractor("O_AMKOR_1");

        for (int i = 1; i <= 4; i++) {
            final Wafer w = new Wafer(i);
            l.getWafers().add(w);
        }

        return l;
    }
}
