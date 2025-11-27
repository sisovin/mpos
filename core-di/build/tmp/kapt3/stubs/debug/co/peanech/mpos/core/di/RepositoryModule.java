package co.peanech.mpos.core.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'\u00a8\u0006\n"}, d2 = {"Lco/peanech/mpos/core/di/RepositoryModule;", "", "()V", "bindCartRepository", "Lco/peanech/mpos/core/data/repository/CartRepository;", "impl", "Lco/peanech/mpos/core/data/repository/InMemoryCartRepository;", "bindProductRepository", "Lco/peanech/mpos/core/data/repository/ProductRepository;", "Lco/peanech/mpos/core/data/repository/FakeProductRepository;", "core-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract co.peanech.mpos.core.data.repository.ProductRepository bindProductRepository(@org.jetbrains.annotations.NotNull
    co.peanech.mpos.core.data.repository.FakeProductRepository impl);
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract co.peanech.mpos.core.data.repository.CartRepository bindCartRepository(@org.jetbrains.annotations.NotNull
    co.peanech.mpos.core.data.repository.InMemoryCartRepository impl);
}