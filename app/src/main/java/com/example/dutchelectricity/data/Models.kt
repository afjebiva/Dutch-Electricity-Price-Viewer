package com.example.dutchelectricity.data

import com.google.gson.annotations.SerializedName

data class QuarterHourPrice(
    @SerializedName("price")
    val price: Double,
    @SerializedName("price_excl_markup")
    val priceExclMarkup: Double,
    @SerializedName("market_price")
    val marketPrice: Double,
    @SerializedName("supplier_markup")
    val supplierMarkup: Double,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("till")
    val till: String,
    @SerializedName("resolution")
    val resolution: String,
    @SerializedName("source")
    val source: String,
    var status: String = "normal"
) {
    // Extract time in HH:mm format from ISO 8601 datetime
    fun getTimeRange(): String {
        return try {
            val fromTime = from.substring(11, 16)
            val tillTime = till.substring(11, 16)
            "$fromTime - $tillTime"
        } catch (e: Exception) {
            from
        }
    }
}
