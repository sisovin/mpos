package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.CheckoutResult
import co.peanech.mpos.core.data.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductRepository @javax.inject.Inject constructor() : ProductRepository {
    private val items = listOf(
        Product(1, "Apple", "Fresh apple", 0.99, null),
        Product(2, "Banana", "Ripe banana", 0.59, null),
        Product(3, "Orange", "Citrus orange", 0.79, null)
    )

    override fun getProducts(): Flow<List<Product>> = flow {
        delay(200)
        emit(items)
    }

    override suspend fun getProduct(id: Long): Product? {
        delay(100)
        return items.find { it.id == id }
    }

    override suspend fun checkout(items: List<Pair<Long, Int>>): CheckoutResult {
        delay(500)
        val total = items.sumOf { (id, qty) ->
            (this.items.find { it.id == id }?.price ?: 0.0) * qty
        }
        return CheckoutResult("ok", total)
    }
}
