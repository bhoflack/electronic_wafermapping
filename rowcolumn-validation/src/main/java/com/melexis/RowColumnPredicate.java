package com.melexis;

import com.melexis.th01.TH01WaferMap;
import com.melexis.th01.exception.Th01Exception;
import com.melexis.util.LotPredicate;

public class RowColumnPredicate implements LotPredicate {

    public boolean apply(final Lot l, final Wafer w, final TH01WaferMap th01) {
        try {
            final int rows = Integer.parseInt(l.getConfig().get("ROWS"));
            final int cols = Integer.parseInt(l.getConfig().get("COLS"));

            return th01.getNumberOfColumns() == cols &&
                th01.getNumberOfRows() == rows;
        } catch(Th01Exception e) {
            throw new RuntimeException("Th01Exception while accessing number of rows and columns for wafermap " + th01, e);
        }
    }
}