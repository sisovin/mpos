package co.peanech.mpos.core.data.model

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val image: String?
)
