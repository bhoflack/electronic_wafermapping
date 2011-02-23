package com.melexis.util;

import com.melexis.Lot;

public interface LotTransformer {

    Lot process(final Lot lot) throws Exception;
}
