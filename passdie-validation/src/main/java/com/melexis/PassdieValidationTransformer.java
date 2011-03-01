package com.melexis;

import com.melexis.th01.TH01WaferMap;
import com.melexis.th01.exception.Th01Exception;
import com.melexis.util.LotTransformer;

import java.util.Map;
import java.util.HashMap;

import static java.lang.String.format;

public class PassdieValidationTransformer implements LotTransformer {

    public Lot process(final Lot lot) {
        for (final Wafer w : lot.getWafers()) {
            final HashMap<String, byte[]> filtered = new HashMap<String, byte[]>();

            for (final String k : w.getWafermaps().keySet()) {
                try {
                    final byte[] v = w.getWafermaps().get(k);
                    final TH01WaferMap wmap = new TH01WaferMap(v);
                    final boolean valid = wmap.getPassCnt() == w.getPassdies();

                    if (valid) {
                        filtered.put(k, v);
                    } else {
                        w.addValidationmessage(format("Wafermap %s contains a wrong number of dies.", k));
                    }
                } catch(Th01Exception e) {
                    throw new RuntimeException(e);
                }
            }

            w.setWafermaps(filtered);
        }

        return lot;
    }
}