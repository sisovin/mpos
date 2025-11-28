package co.peanech.mpos.core.network.api

import co.peanech.mpos.core.network.model.NetworkCheckoutRequest
import co.peanech.mpos.core.network.model.NetworkCheckoutResponse
import co.peanech.mpos.core.network.model.NetworkProduct
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MposApiService {
    @GET("api/products")
    suspend fun getProducts(): List<NetworkProduct>

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Long): NetworkProduct

    @POST("api/checkout")
    suspend fun checkout(@Body request: NetworkCheckoutRequest): NetworkCheckoutResponse
}
