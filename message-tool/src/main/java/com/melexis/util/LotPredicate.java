package com.melexis.util;

import com.melexis.Lot;
import com.melexis.Wafer;

import com.melexis.th01.TH01WaferMap;

public interface LotPredicate {

    /**
     * Predicate for a wafermap.
     * @param l the lot
     * @param w the wafer
     * @param th01 the wafermap
     */
    public boolean apply(final Lot l, final Wafer w, final TH01WaferMap th01);
}