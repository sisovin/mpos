package co.peanech.mpos.feature.cart;

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
public final class CartViewModel_Factory implements Factory<CartViewModel> {
  private final Provider<CartRepository> repositoryProvider;

  private final Provider<ProductRepository> productRepositoryProvider;

  public CartViewModel_Factory(Provider<CartRepository> repositoryProvider,
      Provider<ProductRepository> productRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public CartViewModel get() {
    return newInstance(repositoryProvider.get(), productRepositoryProvider.get());
  }

  public static CartViewModel_Factory create(Provider<CartRepository> repositoryProvider,
      Provider<ProductRepository> productRepositoryProvider) {
    return new CartViewModel_Factory(repositoryProvider, productRepositoryProvider);
  }

  public static CartViewModel newInstance(CartRepository repository,
      ProductRepository productRepository) {
    return new CartViewModel(repository, productRepository);
  }
}
