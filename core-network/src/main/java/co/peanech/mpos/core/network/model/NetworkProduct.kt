package co.peanech.mpos.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String?,
    val images: List<String>? = emptyList(),
    val stock: Int = 0
)
