package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.CheckoutResult
import co.peanech.mpos.core.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun getProduct(id: Long): Product?
    suspend fun checkout(items: List<Pair<Long, Int>>): CheckoutResult
}
