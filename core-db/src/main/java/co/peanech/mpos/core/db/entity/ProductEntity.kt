package co.peanech.mpos.core.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val image: String?
)
