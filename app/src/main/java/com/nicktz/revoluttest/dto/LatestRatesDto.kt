package com.nicktz.revoluttest.dto

import com.google.gson.annotations.SerializedName

data class LatestRatesDto(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
)