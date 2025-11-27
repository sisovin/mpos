package co.peanech.mpos.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.model.CartItem
import co.peanech.mpos.core.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> get() = _items

    init {
        viewModelScope.launch {
            repository.getCartItems().collectLatest {
                _items.value = it
            }
        }
    }

    fun updateQuantity(productId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(productId, quantity)
        }
    }

    fun removeItem(productId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}
