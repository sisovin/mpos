package co.peanech.mpos.feature.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.peanech.mpos.feature.cart.CartViewModel
import co.peanech.mpos.core.data.model.CartItem

@Composable
fun CartScreen(onCheckout: () -> Unit, viewModel: CartViewModel = hiltViewModel()) {
    val items by viewModel.items.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Your Cart", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items) { item ->
                CartItemCard(item = item, onRemove = { viewModel.removeItem(item.product.id) }, onQuantityChanged = { qty -> viewModel.updateQuantity(item.product.id, qty) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = 3.dp) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                val total = items.sumOf { it.product.price * it.quantity }
                Text(text = "Total: $${String.format("%.2f", total)}")
                Button(onClick = onCheckout, enabled = items.isNotEmpty()) {
                    Text(text = "Checkout")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, onRemove: () -> Unit, onQuantityChanged: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.product.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "${item.quantity} x $${item.product.price}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { onQuantityChanged(item.quantity - 1) }) { Text(text = "-") }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.quantity.toString(), modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onQuantityChanged(item.quantity + 1) }) { Text(text = "+") }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onRemove) { Text(text = "Remove") }
            }
        }
    }
}

