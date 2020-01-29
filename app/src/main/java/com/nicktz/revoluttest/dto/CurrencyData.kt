package com.nicktz.revoluttest.dto

import com.nicktz.revoluttest.ui.CurrencyRatesViewModel
import com.nicktz.revoluttest.utils.toBigDecimalWith2digits
import java.math.BigDecimal
import java.math.RoundingMode

data class CurrencyData(
    val amount: Double = CurrencyRatesViewModel.DEFAULT_AMOUNT,
    val base: String = CurrencyRatesViewModel.DEFAULT_CURRENCY,
    val rates: Map<String, Double> = emptyMap()
)