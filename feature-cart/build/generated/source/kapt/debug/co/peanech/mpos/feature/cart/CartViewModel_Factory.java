package co.peanech.mpos.feature.cart;

import co.peanech.mpos.core.data.repository.CartRepository;
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

  public CartViewModel_Factory(Provider<CartRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CartViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CartViewModel_Factory create(Provider<CartRepository> repositoryProvider) {
    return new CartViewModel_Factory(repositoryProvider);
  }

  public static CartViewModel newInstance(CartRepository repository) {
    return new CartViewModel(repository);
  }
}
