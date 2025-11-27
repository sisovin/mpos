package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.CartItem
import co.peanech.mpos.core.data.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product)
    suspend fun updateQuantity(productId: Long, quantity: Int)
    suspend fun removeFromCart(productId: Long)
    suspend fun clearCart()
}
