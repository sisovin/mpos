package com.peanech.mpos.ui.payment

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessPaymentScreen(
    onNavigateBack: () -> Unit,
    onPaymentSuccess: (String, String, Double, Double) -> Unit, // orderId, method, cashReceived, change
    viewModel: ProcessPaymentViewModel = hiltViewModel()
) {
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val cashReceived by viewModel.cashReceived.collectAsState()
    val change by viewModel.change.collectAsState()
    val qrCodeBitmap by viewModel.qrCodeBitmap.collectAsState()
    val paymentStatus by viewModel.paymentStatus.collectAsState()
    val currentOrderId by viewModel.currentOrderId.collectAsState()

    LaunchedEffect(paymentStatus) {
        if (paymentStatus is PaymentStatus.Success) {
            val received = cashReceived.toDoubleOrNull() ?: 0.0
            onPaymentSuccess(currentOrderId, paymentMethod, received, change)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Process Payment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (paymentStatus is PaymentStatus.Processing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Total Amount Header
                Text(
                    text = "Total to Pay",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale.US).format(totalAmount),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider()

                // Payment Method Selector
                Text(
                    text = "Select Payment Method",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentMethodCard(
                        title = "Cash",
                        icon = Icons.Default.Money,
                        selected = paymentMethod == "Cash",
                        onClick = { viewModel.selectPaymentMethod("Cash") },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodCard(
                        title = "Card",
                        icon = Icons.Default.CreditCard,
                        selected = paymentMethod == "Card",
                        onClick = { viewModel.selectPaymentMethod("Card") },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodCard(
                        title = "QR Code",
                        icon = Icons.Default.QrCode,
                        selected = paymentMethod == "QR Code",
                        onClick = { viewModel.selectPaymentMethod("QR Code") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Dynamic Content based on Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        when (paymentMethod) {
                            "Cash" -> CashPaymentView(
                                totalAmount = totalAmount,
                                receivedAmount = cashReceived,
                                changeAmount = change,
                                onReceivedChanged = viewModel::onCashReceivedChanged,
                                onComplete = viewModel::processPayment
                            )
                            "Card" -> CardPaymentView(
                                onPay = viewModel::processPayment
                            )
                            "QR Code" -> QrPaymentView(
                                qrBitmap = qrCodeBitmap,
                                onSimulateScan = viewModel::processPayment
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodCard(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

@Composable
fun CashPaymentView(
    totalAmount: Double,
    receivedAmount: String,
    changeAmount: Double,
    onReceivedChanged: (String) -> Unit,
    onComplete: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Cash Payment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        
        OutlinedTextField(
            value = receivedAmount,
            onValueChange = onReceivedChanged,
            label = { Text("Received Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Change:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = NumberFormat.getCurrencyInstance(Locale.US).format(changeAmount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (changeAmount >= 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = (receivedAmount.toDoubleOrNull() ?: 0.0) >= totalAmount
        ) {
            Text("Complete Payment", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun CardPaymentView(onPay: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Card Payment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("ABA PayWay Gateway", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        
        Icon(
            imageVector = Icons.Default.CreditCard,
            contentDescription = "Card",
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        
        Text(
            text = "Please use the terminal or enter card details securely.",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Button(
            onClick = onPay,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Pay with ABA PayWay", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun QrPaymentView(
    qrBitmap: Bitmap?,
    onSimulateScan: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Scan to Pay", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        
        if (qrBitmap != null) {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier
                    .size(250.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Text(
            text = "Ask customer to scan this QR code",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Button(
            onClick = onSimulateScan,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Simulate Customer Scan", style = MaterialTheme.typography.titleMedium)
        }
    }
}
