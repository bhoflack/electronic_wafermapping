package com.melexis;

import com.melexis.util.LotTransformer;
import com.melexis.wafermaps.WaferService;

import java.util.Map;

public class AttachWafermapsTransformer implements LotTransformer {

    private final WaferService waferService;

    public AttachWafermapsTransformer(WaferService waferService) {
        this.waferService = waferService;
    }

    public Lot process(Lot lot) {
        for (final Wafer w : lot.getWafers()) {
            final Map<String, byte[]> maps = waferService.findWafermapsByLotnameAndWaferid(lot.getName(), w.getWafernumber());
            w.setWafermaps(maps);
        }

        return lot;
    }
}
