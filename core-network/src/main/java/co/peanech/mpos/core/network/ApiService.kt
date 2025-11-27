package co.peanech.mpos.core.network

import retrofit2.http.GET

data class NetworkProduct(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val image: String?
)

interface ApiService {
    @GET("/api/products")
    suspend fun getProducts(): List<NetworkProduct>
}
