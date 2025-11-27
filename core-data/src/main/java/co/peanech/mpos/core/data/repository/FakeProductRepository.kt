package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductRepository @javax.inject.Inject constructor() : ProductRepository {
    override fun getProducts(): Flow<List<Product>> = flow {
        val items = listOf(
            Product(1, "Apple", "Fresh apple", 0.99, null),
            Product(2, "Banana", "Ripe banana", 0.59, null),
            Product(3, "Orange", "Citrus orange", 0.79, null)
        )
        delay(200)
        emit(items)
    }
}
