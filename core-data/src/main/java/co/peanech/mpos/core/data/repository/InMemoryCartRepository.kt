package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.CartItem
import co.peanech.mpos.core.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryCartRepository @javax.inject.Inject constructor() : CartRepository {
    private val mutex = Mutex()
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override fun getCartItems(): Flow<List<CartItem>> = _items.asStateFlow()

    override suspend fun addToCart(product: Product) {
        mutex.withLock {
            val current = _items.value.toMutableList()
            val idx = current.indexOfFirst { it.product.id == product.id }
            if (idx >= 0) {
                val existing = current[idx]
                current[idx] = existing.copy(quantity = existing.quantity + 1)
            } else {
                current.add(CartItem(product, 1))
            }
            _items.value = current.toList()
        }
    }

    override suspend fun updateQuantity(productId: Long, quantity: Int) {
        mutex.withLock {
            val current = _items.value.toMutableList()
            val idx = current.indexOfFirst { it.product.id == productId }
            if (idx >= 0) {
                if (quantity <= 0) {
                    current.removeAt(idx)
                } else {
                    current[idx] = current[idx].copy(quantity = quantity)
                }
            }
            _items.value = current.toList()
        }
    }

    override suspend fun removeFromCart(productId: Long) {
        mutex.withLock {
            _items.value = _items.value.filterNot { it.product.id == productId }
        }
    }

    override suspend fun clearCart() {
        mutex.withLock {
            _items.value = emptyList()
        }
    }
}
