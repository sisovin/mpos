package co.peanech.mpos.feature.cart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0014J\u0006\u0010\u0016\u001a\u00020\u0014J\u000e\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u0019J\u0016\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u001cR\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lco/peanech/mpos/feature/cart/CartViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lco/peanech/mpos/core/data/repository/CartRepository;", "productRepository", "Lco/peanech/mpos/core/data/repository/ProductRepository;", "(Lco/peanech/mpos/core/data/repository/CartRepository;Lco/peanech/mpos/core/data/repository/ProductRepository;)V", "_checkoutStatus", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_items", "", "Lco/peanech/mpos/core/data/model/CartItem;", "checkoutStatus", "Lkotlinx/coroutines/flow/StateFlow;", "getCheckoutStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "items", "getItems", "checkout", "", "clearCart", "clearCheckoutStatus", "removeItem", "productId", "", "updateQuantity", "quantity", "", "feature-cart_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CartViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final co.peanech.mpos.core.data.repository.CartRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final co.peanech.mpos.core.data.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<co.peanech.mpos.core.data.model.CartItem>> _items = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _checkoutStatus = null;
    
    @javax.inject.Inject()
    public CartViewModel(@org.jetbrains.annotations.NotNull()
    co.peanech.mpos.core.data.repository.CartRepository repository, @org.jetbrains.annotations.NotNull()
    co.peanech.mpos.core.data.repository.ProductRepository productRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<co.peanech.mpos.core.data.model.CartItem>> getItems() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getCheckoutStatus() {
        return null;
    }
    
    public final void checkout() {
    }
    
    public final void clearCheckoutStatus() {
    }
    
    public final void updateQuantity(long productId, int quantity) {
    }
    
    public final void removeItem(long productId) {
    }
    
    public final void clearCart() {
    }
}