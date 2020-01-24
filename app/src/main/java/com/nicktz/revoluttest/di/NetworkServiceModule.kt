package com.nicktz.revoluttest.di

import com.nicktz.revoluttest.api.RevolutApi
import org.koin.dsl.module

val networkServiceModule = module {
    single { RevolutApi.create() }
}