package com.peanech.mpos.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.model.CartItem
import co.peanech.mpos.core.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _subtotal = MutableStateFlow(0.0)
    val subtotal: StateFlow<Double> = _subtotal.asStateFlow()

    private val _tax = MutableStateFlow(0.0)
    val tax: StateFlow<Double> = _tax.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    // Form State
    var fullName = MutableStateFlow("")
    var email = MutableStateFlow("")
    var phone = MutableStateFlow("")
    var address = MutableStateFlow("")
    var paymentMethod = MutableStateFlow("Cash") // Cash, Card, QR Code

    init {
        viewModelScope.launch {
            cartRepository.getCartItems().collectLatest { items ->
                _cartItems.value = items
                calculateTotals(items)
            }
        }
    }

    private fun calculateTotals(items: List<CartItem>) {
        val sub = items.sumOf { it.product.price * it.quantity }
        val tx = sub * 0.1 // 10% tax
        _subtotal.value = sub
        _tax.value = tx
        _total.value = sub + tx
    }

    fun placeOrder() {
        // TODO: Implement actual order placement logic
        viewModelScope.launch {
            // Simulate network delay
            // Clear cart on success
            cartRepository.clearCart()
        }
    }
}
