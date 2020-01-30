package com.nicktz.revoluttest.ui

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nicktz.revoluttest.data.CurrencyRateRepository
import com.nicktz.revoluttest.utils.FakeDataFactory
import com.nicktz.revoluttest.utils.RxImmediateSchedulerRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O], manifest = Config.NONE)
class CurrencyRatesViewModelTest {

    @Rule
    @JvmField
    val scheduler = RxImmediateSchedulerRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    internal lateinit var repository: CurrencyRateRepository

    private lateinit var viewModel: CurrencyRatesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun verifyOnCurrencyClicked() {
        every { repository.getLatestRates("EUR") } returns Single.fromCallable { FakeDataFactory.createLatestRatesDto() }
        viewModel = spyk(CurrencyRatesViewModel(repository))
        // TODO: test OnCurrencyClicked
        confirmVerified(viewModel)
    }
}