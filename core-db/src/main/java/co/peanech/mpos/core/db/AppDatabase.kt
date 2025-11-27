package co.peanech.mpos.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import co.peanech.mpos.core.db.dao.ProductDao
import co.peanech.mpos.core.db.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
