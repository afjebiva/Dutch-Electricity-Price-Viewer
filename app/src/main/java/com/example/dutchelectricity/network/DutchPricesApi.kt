package com.example.dutchelectricity.network

import com.example.dutchelectricity.data.QuarterHourPrice
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DutchPricesApi {
    @GET("api/stroom/kwartierprijzen")
    fun getQuarterHourPrices(@Query("uren") hours: Int = 24): Call<List<QuarterHourPrice>>

    @GET("api/stroom/actueel")
    fun getCurrentPrice(): Call<QuarterHourPrice>
}
