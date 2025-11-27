package co.peanech.mpos.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.model.Product
import co.peanech.mpos.core.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cartRepository: co.peanech.mpos.core.data.repository.CartRepository
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products
    private val _cartItemCount = kotlinx.coroutines.flow.MutableStateFlow(0)
    val cartItemCount: kotlinx.coroutines.flow.StateFlow<Int> get() = _cartItemCount
    

    init {
        viewModelScope.launch {
            repository.getProducts().collectLatest { items ->
                _products.value = items
            }
        }
        viewModelScope.launch {
            cartRepository.getCartItems().collectLatest { items ->
                _cartItemCount.value = items.sumOf { it.quantity }
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
        }
    }
}
