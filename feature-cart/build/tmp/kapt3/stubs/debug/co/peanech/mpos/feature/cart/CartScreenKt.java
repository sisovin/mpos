package co.peanech.mpos.feature.cart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a2\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0007\u001a2\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00042\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a.\u0010\u000e\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0016\u0010\u0012\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0007\u001a\"\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0007\u00a8\u0006\u001a"}, d2 = {"CartBottomBar", "", "items", "", "Lco/peanech/mpos/core/data/model/CartItem;", "onClear", "Lkotlin/Function0;", "onCheckout", "CartItemCard", "item", "onRemove", "onQuantityChanged", "Lkotlin/Function1;", "", "CartScreen", "onClose", "viewModel", "Lco/peanech/mpos/feature/cart/CartViewModel;", "CartSummary", "SummaryRow", "label", "", "amount", "", "isTotal", "", "feature-cart_debug"})
public final class CartScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CartScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCheckout, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull()
    co.peanech.mpos.feature.cart.CartViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CartItemCard(@org.jetbrains.annotations.NotNull()
    co.peanech.mpos.core.data.model.CartItem item, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onQuantityChanged) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CartSummary(@org.jetbrains.annotations.NotNull()
    java.util.List<co.peanech.mpos.core.data.model.CartItem> items) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SummaryRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, double amount, boolean isTotal) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CartBottomBar(@org.jetbrains.annotations.NotNull()
    java.util.List<co.peanech.mpos.core.data.model.CartItem> items, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClear, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCheckout) {
    }
}