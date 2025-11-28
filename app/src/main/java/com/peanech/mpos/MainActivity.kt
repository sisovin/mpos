package com.peanech.mpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peanech.mpos.ui.splash.SplashScreen
import com.peanech.mpos.ui.onboarding.OnboardingScreen
import com.peanech.mpos.ui.auth.SigninScreen
import com.peanech.mpos.ui.auth.SignupScreen
import com.peanech.mpos.ui.dashboard.DashboardScreen
import co.peanech.mpos.feature.products.ProductsScreen
import co.peanech.mpos.feature.cart.CartScreen
import com.peanech.mpos.ui.theme.MPosTheme
import dagger.hilt.android.AndroidEntryPoint

import com.peanech.mpos.ui.checkout.CheckoutScreen
import com.peanech.mpos.ui.placeorder.PlaceOrderScreen
import com.peanech.mpos.ui.payment.ProcessPaymentScreen
import com.peanech.mpos.ui.invoice.InvoiceScreen
import com.peanech.mpos.ui.afterprinting.AfterPrintingDashboardScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MPosTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(onTimeout = { navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }})
                    }
                    composable("onboarding") {
                        OnboardingScreen(
                            onGetStarted = { navController.navigate("signin") },
                            onSignUp = { navController.navigate("signup") }
                        )
                    }
                    composable("signin") {
                        SigninScreen(
                            onSignInSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            },
                            onNavigateToSignUp = { navController.navigate("signup") }
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            onSignUpSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            },
                            onNavigateToSignIn = { navController.navigate("signin") }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToProducts = { navController.navigate("products") },
                            onNavigateToInvoice = { navController.navigate("cart") }, // Using cart for now
                            onNavigateToReports = { /* TODO: Implement Reports screen */ }
                        )
                    }
                    composable("products") {
                        ProductsScreen(onOpenCart = { navController.navigate("cart") })
                    }
                    composable("cart") {
                        CartScreen(
                            onCheckout = { navController.navigate("place_order") },
                            onClose = { navController.popBackStack() }
                        )
                    }
                    composable("place_order") {
                        PlaceOrderScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onPlaceOrder = { total ->
                                navController.navigate("payment/$total")
                            }
                        )
                    }
                    composable("checkout") {
                        // Deprecated or used as review screen? For now, let's keep it but Cart goes to PlaceOrder
                        CheckoutScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onOrderPlaced = {
                                navController.navigate("payment/0.0") // Fallback
                            }
                        )
                    }
                    composable("payment/{totalAmount}") { backStackEntry ->
                        val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
                        ProcessPaymentScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onPaymentSuccess = { orderId, method, cash, change ->
                                navController.navigate("invoice/$orderId/$totalAmount/$method/$cash/$change") {
                                    popUpTo("payment/{totalAmount}") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("invoice/{orderId}/{totalAmount}/{paymentMethod}/{cashReceived}/{change}") {
                        InvoiceScreen(
                            onNavigateBack = { navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } } },
                            onPrint = { orderId, totalAmount ->
                                navController.navigate("after_print/$orderId/$totalAmount")
                            }
                        )
                    }
                    composable("after_print/{orderId}/{totalAmount}") {
                        AfterPrintingDashboardScreen(
                            onNewSale = {
                                navController.navigate("dashboard") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            },
                            onReprint = { /* TODO: Implement Reprint Logic */ }
                        )
                    }
                }
            }
        }
    }
}