package com.melexis.util;

import com.google.common.collect.ImmutableMap;
import com.melexis.Lot;
import com.melexis.Wafer;
import com.melexis.th01.TH01WaferMap;

import static java.lang.String.format;

public class LotValidation implements LotTransformer {

    private final String validationTemplate;
    private final LotPredicate predicate;

    public LotValidation(final LotPredicate predicate, final String template) {
        this.predicate = predicate;
        this.validationTemplate = template;
    }

    public Lot process(final Lot l) {
        for (final Wafer w : l.getWafers()) {
            final ImmutableMap.Builder<String, byte[]> filtered = new ImmutableMap.Builder<String, byte[]>();

            for (final String k : w.getWafermaps().keySet()) {
                final byte[] v = w.getWafermaps().get(k);
                final TH01WaferMap wmap = new TH01WaferMap(v);

                final boolean valid = predicate.apply(l, w, wmap);

                if (valid) {
                    filtered.put(k, v);
                } else {
                    w.addValidationmessage(format(validationTemplate, k));
                }
            }

            w.setWafermaps(filtered.build());
        }

        return l;
    }
}