package co.peanech.mpos.feature.cart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001a \u0010\t\u001a\u00020\u00012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u00a8\u0006\r"}, d2 = {"CartItemCard", "", "item", "Lco/peanech/mpos/core/data/model/CartItem;", "onRemove", "Lkotlin/Function0;", "onQuantityChanged", "Lkotlin/Function1;", "", "CartScreen", "onCheckout", "viewModel", "Lco/peanech/mpos/feature/cart/CartViewModel;", "feature-cart_debug"})
public final class CartScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void CartScreen(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onCheckout, @org.jetbrains.annotations.NotNull
    co.peanech.mpos.feature.cart.CartViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void CartItemCard(@org.jetbrains.annotations.NotNull
    co.peanech.mpos.core.data.model.CartItem item, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onQuantityChanged) {
    }
}