package co.peanech.mpos.core.data.di

import co.peanech.mpos.core.data.repository.NetworkProductRepository
import co.peanech.mpos.core.data.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindProductRepository(
        networkProductRepository: NetworkProductRepository
    ): ProductRepository

    @Binds
    fun bindCartRepository(
        inMemoryCartRepository: co.peanech.mpos.core.data.repository.InMemoryCartRepository
    ): co.peanech.mpos.core.data.repository.CartRepository
}
