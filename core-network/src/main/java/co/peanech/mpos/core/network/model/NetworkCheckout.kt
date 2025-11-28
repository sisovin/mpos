package co.peanech.mpos.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCheckoutRequest(
    val items: List<NetworkCartItem>
)

@Serializable
data class NetworkCartItem(
    val productId: Long,
    val quantity: Int
)

@Serializable
data class NetworkCheckoutResponse(
    val status: String,
    val total: Double
)
