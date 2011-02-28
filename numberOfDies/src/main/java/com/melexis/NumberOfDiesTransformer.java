package com.melexis;

import com.melexis.th01.TH01WaferMap;
import com.melexis.th01.exception.Th01Exception;
import com.melexis.util.LotTransformer;

public class NumberOfDiesTransformer implements LotTransformer {

    public Lot process(final Lot lot) {
        for (final Wafer wafer : lot.getWafers()) {
            final byte[] wmap = first(wafer.getWafermaps().values());
            try {
                final TH01WaferMap wafermap = new TH01WaferMap(wmap);
                wafer.setPassdies(wafermap.getPassCnt());
            } catch(Th01Exception e) {
                throw new RuntimeException(e);
            }
        }

        return lot;
    }

    private final static byte[] first(final Iterable<byte[]> b) {
        return b.iterator().next();
    }
}