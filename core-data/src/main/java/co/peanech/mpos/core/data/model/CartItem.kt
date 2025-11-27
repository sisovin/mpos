package co.peanech.mpos.core.data.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)
