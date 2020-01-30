package com.nicktz.revoluttest.data

import com.nicktz.revoluttest.api.RevolutApi
import org.junit.Test
import com.nicktz.revoluttest.utils.FakeDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single

class CurrencyRateRepositoryTest {

    private val revolutApiMock = mockk<RevolutApi>()
    private val repository = CurrencyRateRepository(revolutApiMock)

    @Test
    fun verifyCompleteRates() {
        every { revolutApiMock.getLatestRates("EUR") } returns Single.fromCallable { FakeDataFactory.createLatestRatesDto() }
        val testObserver = repository.getLatestRates("EUR").test()

        verify(exactly = 1) { revolutApiMock.getLatestRates("EUR") }

        testObserver.assertValue(FakeDataFactory.createLatestRatesDto())

        testObserver.dispose()
    }

    @Test
    fun verifyError() {
        every { revolutApiMock.getLatestRates("EUR") } returns Single.error(Throwable("network issue"))
        val testObserver = repository.getLatestRates("EUR").test()

        verify(exactly = 1) { revolutApiMock.getLatestRates("EUR") }

        testObserver.assertError(Throwable::class.java)

        testObserver.dispose()
    }
}