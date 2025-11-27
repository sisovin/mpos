package co.peanech.mpos.core.di

import co.peanech.mpos.core.data.repository.FakeProductRepository
import co.peanech.mpos.core.data.repository.ProductRepository
import co.peanech.mpos.core.data.repository.CartRepository
import co.peanech.mpos.core.data.repository.InMemoryCartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: FakeProductRepository): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: InMemoryCartRepository): CartRepository
}
