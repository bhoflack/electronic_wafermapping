package com.melexis;

import com.melexis.th01.TH01WaferMap;
import com.melexis.th01.exception.Th01Exception;
import com.melexis.util.LotPredicate;

public class PostprocessingPredicate implements LotPredicate {

    public boolean apply(final Lot l, final Wafer w, final TH01WaferMap th01) {
        try {
            return th01.getProberId().equals("INK_TH01");
        } catch(Th01Exception e) {
            throw new RuntimeException("Th01Exception while processing wafermap " + th01, e);
        }
    }

}