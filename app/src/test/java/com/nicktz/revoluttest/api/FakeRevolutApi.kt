package com.nicktz.revoluttest.api

import com.nicktz.revoluttest.utils.FakeDataFactory
import com.nicktz.revoluttest.dto.LatestRatesDto
import io.reactivex.Single

class FakeRevolutApi : RevolutApi {

    private var latestRatesDto: LatestRatesDto? = null

    override fun getLatestRates(currency: String): Single<LatestRatesDto> =
        Single.just(latestRatesDto)

    fun generateFakeData() {
        latestRatesDto = FakeDataFactory.createLatestRatesDto()
    }
}