package com.example.dutchelectricity.repository

import com.example.dutchelectricity.data.PriceData
import com.example.dutchelectricity.network.RetrofitClient
import retrofit2.awaitResponse

class PriceRepository {
    suspend fun fetchPrices(): Result<List<PriceData>> = try {
        val response = RetrofitClient.apiService.getPrices().awaitResponse()
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!.prices)
        } else {
            Result.failure(Exception("API Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun highlightPrices(
        prices: List<PriceData>,
        cheapestCount: Int,
        expensiveCount: Int
    ): List<PriceData> {
        if (prices.isEmpty()) return prices

        // Sort by price to identify cheapest and most expensive
        val sortedByPrice = prices.sortedBy { it.price }
        val sortedByPriceDesc = prices.sortedByDescending { it.price }

        val cheapestPrices = sortedByPrice.take(cheapestCount).map { it.price }.toSet()
        val expensivePrices = sortedByPriceDesc.take(expensiveCount).map { it.price }.toSet()

        return prices.map { price ->
            when {
                price.price in cheapestPrices -> price.copy(status = "cheap")
                price.price in expensivePrices -> price.copy(status = "expensive")
                else -> price.copy(status = "normal")
            }
        }
    }
}
