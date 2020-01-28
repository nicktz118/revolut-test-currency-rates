package com.nicktz.revoluttest.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.toBigDecimalWith2digits(): BigDecimal {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN)
}