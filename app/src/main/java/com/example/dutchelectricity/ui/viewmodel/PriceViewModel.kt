package com.example.dutchelectricity.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dutchelectricity.data.PriceData
import com.example.dutchelectricity.repository.PriceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PriceViewModel : ViewModel() {
    private val repository = PriceRepository()

    private val _prices = MutableStateFlow<List<PriceData>>(emptyList())
    val prices: StateFlow<List<PriceData>> = _prices

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cheapestCount = MutableStateFlow(3)
    val cheapestCount: StateFlow<Int> = _cheapestCount

    private val _expensiveCount = MutableStateFlow(3)
    val expensiveCount: StateFlow<Int> = _expensiveCount

    fun updateCheapestCount(count: Int) {
        _cheapestCount.value = count
        applyHighlighting()
    }

    fun updateExpensiveCount(count: Int) {
        _expensiveCount.value = count
        applyHighlighting()
    }

    fun fetchPrices() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.fetchPrices()
            result.onSuccess { priceList ->
                _prices.value = priceList
                applyHighlighting()
            }.onFailure { exception ->
                _error.value = exception.message ?: "Unknown error occurred"
            }
            _isLoading.value = false
        }
    }

    private fun applyHighlighting() {
        _prices.value = repository.highlightPrices(
            _prices.value,
            _cheapestCount.value,
            _expensiveCount.value
        )
    }
}
