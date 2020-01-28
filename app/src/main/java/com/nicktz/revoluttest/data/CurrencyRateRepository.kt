package com.nicktz.revoluttest.data

import com.nicktz.revoluttest.api.RevolutApi
import com.nicktz.revoluttest.dto.LatestRatesDto
import io.reactivex.Single

class CurrencyRateRepository(val revolutApi: RevolutApi) {

    fun getLatestRates(currency: String): Single<LatestRatesDto> {
        return revolutApi.getLatestRates(currency)
    }
}