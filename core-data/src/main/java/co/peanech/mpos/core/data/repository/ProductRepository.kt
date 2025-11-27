package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
}
