package co.peanech.mpos.feature.products;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lco/peanech/mpos/feature/products/ProductsViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lco/peanech/mpos/core/data/repository/ProductRepository;", "cartRepository", "Lco/peanech/mpos/core/data/repository/CartRepository;", "(Lco/peanech/mpos/core/data/repository/ProductRepository;Lco/peanech/mpos/core/data/repository/CartRepository;)V", "_cartItemCount", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_products", "", "Lco/peanech/mpos/core/data/model/Product;", "cartItemCount", "Lkotlinx/coroutines/flow/StateFlow;", "getCartItemCount", "()Lkotlinx/coroutines/flow/StateFlow;", "products", "getProducts", "addToCart", "", "product", "feature-products_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class ProductsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final co.peanech.mpos.core.data.repository.ProductRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final co.peanech.mpos.core.data.repository.CartRepository cartRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<co.peanech.mpos.core.data.model.Product>> _products = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _cartItemCount = null;
    
    @javax.inject.Inject
    public ProductsViewModel(@org.jetbrains.annotations.NotNull
    co.peanech.mpos.core.data.repository.ProductRepository repository, @org.jetbrains.annotations.NotNull
    co.peanech.mpos.core.data.repository.CartRepository cartRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<co.peanech.mpos.core.data.model.Product>> getProducts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getCartItemCount() {
        return null;
    }
    
    public final void addToCart(@org.jetbrains.annotations.NotNull
    co.peanech.mpos.core.data.model.Product product) {
    }
}