package com.nicktz.revoluttest.dto

import com.nicktz.revoluttest.ui.CurrencyRatesViewModel
import com.nicktz.revoluttest.utils.toBigDecimalWith2digits
import java.math.BigDecimal
import java.math.RoundingMode

data class CurrencyData(
    var amount: Double = CurrencyRatesViewModel.DEFAULT_AMOUNT,
    var base: String = CurrencyRatesViewModel.DEFAULT_CURRENCY,
    var rates: Map<String, Double> = emptyMap()
)