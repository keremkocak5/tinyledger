package com.tiny.ledger.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;


public class Constants {

    private Constants() {
    }

    public static final String GBP = "GBP";

    private static final RoundingMode AMOUNT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final int AMOUNT_SCALE = 2;
    public static final Function<BigDecimal, BigDecimal> ROUNDING_FUNCTION = (bigDecimal -> bigDecimal.setScale(AMOUNT_SCALE, AMOUNT_ROUNDING_MODE));

}
