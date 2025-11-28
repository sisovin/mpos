package co.peanech.mpos.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.model.Product
import co.peanech.mpos.core.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
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
    private val _searchQuery = MutableStateFlow("")
    
    val products: StateFlow<List<Product>> = kotlinx.coroutines.flow.combine(_products, _searchQuery) { products, query ->
        if (query.isBlank()) {
            products
        } else {
            products.filter { it.title.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cartItemCount = kotlinx.coroutines.flow.MutableStateFlow(0)
    val cartItemCount: kotlinx.coroutines.flow.StateFlow<Int> get() = _cartItemCount
    
    private val _cartTotal = kotlinx.coroutines.flow.MutableStateFlow(0.0)
    val cartTotal: kotlinx.coroutines.flow.StateFlow<Double> get() = _cartTotal

    init {
        viewModelScope.launch {
            repository.getProducts().collectLatest { items ->
                _products.value = items
            }
        }
        viewModelScope.launch {
            cartRepository.getCartItems().collectLatest { items ->
                _cartItemCount.value = items.sumOf { it.quantity }
                _cartTotal.value = items.sumOf { it.product.price * it.quantity }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
        }
    }
}
