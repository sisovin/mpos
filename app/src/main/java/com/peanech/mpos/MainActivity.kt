package com.peanech.mpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peanech.mpos.ui.splash.SplashScreen
import co.peanech.mpos.feature.products.ProductsScreen
import co.peanech.mpos.feature.cart.CartScreen
import com.peanech.mpos.ui.theme.MPosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MPosTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(onTimeout = { navController.navigate("products") {
                            popUpTo("splash") { inclusive = true }
                        }})
                    }
                    composable("products") {
                        ProductsScreen(onOpenCart = { navController.navigate("cart") })
                    }
                    composable("cart") {
                        CartScreen(onCheckout = { /* TODO: Checkout flow */ })
                    }
                }
            }
        }
    }
}