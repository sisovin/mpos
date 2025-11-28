package com.peanech.mpos.ui.invoice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.model.CartItem
import co.peanech.mpos.core.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _orderId = MutableStateFlow("")
    val orderId: StateFlow<String> = _orderId.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    private val _paymentMethod = MutableStateFlow("")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    private val _cashReceived = MutableStateFlow(0.0)
    val cashReceived: StateFlow<Double> = _cashReceived.asStateFlow()

    private val _change = MutableStateFlow(0.0)
    val change: StateFlow<Double> = _change.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        _orderId.value = savedStateHandle.get<String>("orderId") ?: "UNKNOWN"
        val amountStr = savedStateHandle.get<String>("totalAmount")
        _totalAmount.value = amountStr?.toDoubleOrNull() ?: 0.0

        _paymentMethod.value = savedStateHandle.get<String>("paymentMethod") ?: ""
        val cashStr = savedStateHandle.get<String>("cashReceived")
        _cashReceived.value = cashStr?.toDoubleOrNull() ?: 0.0
        val changeStr = savedStateHandle.get<String>("change")
        _change.value = changeStr?.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }
}
