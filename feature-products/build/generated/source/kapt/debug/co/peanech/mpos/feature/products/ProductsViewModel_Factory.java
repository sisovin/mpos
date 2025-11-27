package co.peanech.mpos.feature.products;

import co.peanech.mpos.core.data.repository.CartRepository;
import co.peanech.mpos.core.data.repository.ProductRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ProductsViewModel_Factory implements Factory<ProductsViewModel> {
  private final Provider<ProductRepository> repositoryProvider;

  private final Provider<CartRepository> cartRepositoryProvider;

  public ProductsViewModel_Factory(Provider<ProductRepository> repositoryProvider,
      Provider<CartRepository> cartRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.cartRepositoryProvider = cartRepositoryProvider;
  }

  @Override
  public ProductsViewModel get() {
    return newInstance(repositoryProvider.get(), cartRepositoryProvider.get());
  }

  public static ProductsViewModel_Factory create(Provider<ProductRepository> repositoryProvider,
      Provider<CartRepository> cartRepositoryProvider) {
    return new ProductsViewModel_Factory(repositoryProvider, cartRepositoryProvider);
  }

  public static ProductsViewModel newInstance(ProductRepository repository,
      CartRepository cartRepository) {
    return new ProductsViewModel(repository, cartRepository);
  }
}
