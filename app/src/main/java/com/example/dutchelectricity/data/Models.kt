package com.example.dutchelectricity.data

data class PriceData(
    val timestamp: String,
    val price: Double,
    val status: String = "normal"
)

data class PricingResponse(
    val prices: List<PriceData>
)
