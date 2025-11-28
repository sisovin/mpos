package co.peanech.mpos.core.data.repository

import co.peanech.mpos.core.data.model.CheckoutResult
import co.peanech.mpos.core.data.model.Product
import co.peanech.mpos.core.network.api.MposApiService
import co.peanech.mpos.core.network.model.NetworkCartItem
import co.peanech.mpos.core.network.model.NetworkCheckoutRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkProductRepository @Inject constructor(
    private val apiService: MposApiService
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> = flow {
        val networkProducts = apiService.getProducts()
        val domainProducts = networkProducts.map {
            Product(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                image = it.thumbnail
            )
        }
        emit(domainProducts)
    }

    override suspend fun getProduct(id: Long): Product? {
        return try {
            val networkProduct = apiService.getProduct(id)
            Product(
                id = networkProduct.id,
                title = networkProduct.title,
                description = networkProduct.description,
                price = networkProduct.price,
                image = networkProduct.thumbnail
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun checkout(items: List<Pair<Long, Int>>): CheckoutResult {
        val networkItems = items.map { (id, qty) ->
            NetworkCartItem(id, qty)
        }
        val response = apiService.checkout(NetworkCheckoutRequest(networkItems))
        return CheckoutResult(response.status, response.total)
    }
}
