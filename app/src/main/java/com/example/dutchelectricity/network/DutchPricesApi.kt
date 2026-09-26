package com.example.dutchelectricity.network

import com.example.dutchelectricity.data.PricingResponse
import retrofit2.Call
import retrofit2.http.GET

interface DutchPricesApi {
    @GET("v1/electricity")
    fun getPrices(): Call<PricingResponse>
}
