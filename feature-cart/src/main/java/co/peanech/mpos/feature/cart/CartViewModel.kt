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
    private val repository: CartRepository,
    private val productRepository: co.peanech.mpos.core.data.repository.ProductRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> get() = _items
    private val _checkoutStatus = MutableStateFlow<String?>(null)
    val checkoutStatus: StateFlow<String?> get() = _checkoutStatus

    init {
        viewModelScope.launch {
            repository.getCartItems().collectLatest {
                _items.value = it
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            val currentItems = _items.value.map { it.product.id to it.quantity }
            if (currentItems.isNotEmpty()) {
                try {
                    val result = productRepository.checkout(currentItems)
                    _checkoutStatus.value = "Success: ${result.total}"
                    repository.clearCart()
                } catch (e: Exception) {
                    _checkoutStatus.value = "Error: ${e.message}"
                }
            }
        }
    }

    fun clearCheckoutStatus() {
        _checkoutStatus.value = null
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
