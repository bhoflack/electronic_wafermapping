package com.melexis;

import com.melexis.th01.TH01WaferMap;
import com.melexis.th01.exception.Th01Exception;
import com.melexis.util.LotPredicate;

public class PassdiePredicate implements LotPredicate {

    public boolean apply(Lot l, Wafer w, TH01WaferMap th01) {
        try {
            return th01.getPassCnt() == w.getPassdies();
        } catch (Th01Exception e) {
            throw new RuntimeException("Th01Exception occured while checking the pass dies.", e);
        }
    }
}
