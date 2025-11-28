package com.peanech.mpos.ui.afterprinting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AfterPrintingViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _orderId = MutableStateFlow("")
    val orderId: StateFlow<String> = _orderId.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    init {
        _orderId.value = savedStateHandle.get<String>("orderId") ?: "UNKNOWN"
        val amountStr = savedStateHandle.get<String>("totalAmount")
        _totalAmount.value = amountStr?.toDoubleOrNull() ?: 0.0
    }

    fun startNewSale() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
