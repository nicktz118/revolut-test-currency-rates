package com.nicktz.revoluttest.di

import com.nicktz.revoluttest.data.CurrencyRateRepository
import com.nicktz.revoluttest.ui.CurrencyRatesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { CurrencyRateRepository(revolutApi = get()) }
    viewModel { CurrencyRatesViewModel(get()) }
}