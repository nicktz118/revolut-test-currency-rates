package com.nicktz.revoluttest.utils

import com.nicktz.revoluttest.dto.LatestRatesDto
import com.nicktz.revoluttest.ui.CurrencyRateDisplayItem

/** {
   "base":"EUR",
   "date":"2018-09-06",
   "rates":{
      "AUD":1.6116,
      "BGN":1.95,
      "BRL":4.7775,
      "CAD":1.5292,
      "CHF":1.1241,
      "CNY":7.9214,
      "CZK":25.638,
      "DKK":7.4345,
      "GBP":0.89556,
      "HKD":9.1052,
      "HRK":7.412,
      "HUF":325.52,
      "IDR":17272.0,
      "ILS":4.1582,
      "INR":83.468,
      "ISK":127.42,
      "JPY":129.16,
      "KRW":1300.9,
      "MXN":22.299,
      "MYR":4.7977,
      "NOK":9.7469,
      "NZD":1.758,
      "PHP":62.406,
      "PLN":4.3054,
      "RON":4.6247,
      "RUB":79.338,
      "SEK":10.559,
      "SGD":1.5952,
      "THB":38.016,
      "TRY":7.6055,
      "USD":1.1599,
      "ZAR":17.77
   }
}
* */
object FakeDataFactory {

    fun createRateDisplayItems(amount: Double): List<CurrencyRateDisplayItem> {
        val latestRatesDto =
            createLatestRatesDto()
        val base = CurrencyRateDisplayItem
            .createBaseCurrency(amount, latestRatesDto.base)
        val displayItems =
            latestRatesDto.rates.map {
                CurrencyRateDisplayItem.createByCurrencyRate(amount, it.key, it.value)
            }
        return listOf(base).plus(displayItems)
    }

    fun createLatestRatesDto(): LatestRatesDto =
        LatestRatesDto(
            base = "EUR",
            date = "2018-09-06",
            rates = mapOf(
                "AUD" to 1.6116,
                "BGN" to 1.95,
                "BRL" to 4.7775,
                "CAD" to 1.5292,
                "CHF" to 1.1241,
                "CNY" to 7.9214,
                "CZK" to 25.638,
                "DKK" to 7.4345,
                "GBP" to 0.89556,
                "HKD" to 9.1052,
                "HRK" to 7.412,
                "HUF" to 325.52,
                "IDR" to 17272.0,
                "ILS" to 4.1582,
                "INR" to 83.468,
                "ISK" to 127.42,
                "JPY" to 129.16,
                "KRW" to 1300.9,
                "MXN" to 22.299,
                "MYR" to 4.7977,
                "NOK" to 9.7469,
                "NZD" to 1.758,
                "PHP" to 62.406,
                "PLN" to 4.3054,
                "RON" to 4.6247,
                "RUB" to 79.338,
                "SEK" to 10.559,
                "SGD" to 1.5952,
                "THB" to 38.016,
                "TRY" to 7.6055,
                "USD" to 1.1599,
                "ZAR" to 17.77
            )
        )
}