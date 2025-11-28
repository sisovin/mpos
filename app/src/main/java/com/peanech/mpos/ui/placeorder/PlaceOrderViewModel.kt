package com.peanech.mpos.ui.placeorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceOrderViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // Form State
    val customerName = MutableStateFlow("")
    val phoneNumber = MutableStateFlow("")
    val email = MutableStateFlow("")
    val deliveryType = MutableStateFlow(DeliveryType.PICKUP)
    val note = MutableStateFlow("")

    // Tip State
    val tipType = MutableStateFlow(TipType.PERCENTAGE)
    val tipValue = MutableStateFlow(0.0) // Percentage (e.g. 10.0) or Fixed Amount
    val selectedTipOption = MutableStateFlow<TipOption>(TipOption.None)

    // Totals
    private val _cartSubtotal = MutableStateFlow(0.0)
    val cartSubtotal: StateFlow<Double> = _cartSubtotal.asStateFlow()

    private val _tax = MutableStateFlow(0.0)
    val tax: StateFlow<Double> = _tax.asStateFlow()

    private val _calculatedTip = MutableStateFlow(0.0)
    val calculatedTip: StateFlow<Double> = _calculatedTip.asStateFlow()

    private val _finalTotal = MutableStateFlow(0.0)
    val finalTotal: StateFlow<Double> = _finalTotal.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                cartRepository.getCartItems(),
                tipType,
                tipValue
            ) { items, type, value ->
                val sub = items.sumOf { it.product.price * it.quantity }
                val tx = sub * 0.1 // 10% Tax
                
                val tipAmount = if (type == TipType.PERCENTAGE) {
                    sub * (value / 100.0)
                } else {
                    value
                }

                Triple(sub, tx, tipAmount)
            }.collect { (sub, tx, tip) ->
                _cartSubtotal.value = sub
                _tax.value = tx
                _calculatedTip.value = tip
                _finalTotal.value = sub + tx + tip
            }
        }
    }

    fun setTipOption(option: TipOption) {
        selectedTipOption.value = option
        when (option) {
            is TipOption.Percent -> {
                tipType.value = TipType.PERCENTAGE
                tipValue.value = option.percent
            }
            is TipOption.Custom -> {
                // Handle custom logic if needed, for now assume fixed or percent input
                tipType.value = TipType.FIXED
                tipValue.value = 0.0
            }
            TipOption.None -> {
                tipType.value = TipType.FIXED
                tipValue.value = 0.0
            }
        }
    }

    fun setCustomTip(amount: Double) {
        selectedTipOption.value = TipOption.Custom
        tipType.value = TipType.FIXED
        tipValue.value = amount
    }
}

enum class DeliveryType {
    PICKUP, DELIVERY
}

enum class TipType {
    PERCENTAGE, FIXED
}

sealed class TipOption {
    object None : TipOption()
    data class Percent(val percent: Double) : TipOption()
    object Custom : TipOption()
}
