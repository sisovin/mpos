package com.peanech.mpos.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToProducts: () -> Unit = {},
    onNavigateToInvoice: () -> Unit = {},
    onNavigateToReports: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DashboardHeader(
                dayStatus = uiState.dayStatus,
                onProfileClick = { /* TODO */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Quick Area
            TopQuickArea(
                dayStatus = uiState.dayStatus,
                onStartDayClick = { /* Focus on Open Day card or just scroll to it */ },
                onSearchClick = { /* TODO: Navigate to search */ }
            )

            // Open Day Card or Day Summary
            if (uiState.dayStatus == DayStatus.CLOSED) {
                OpenDayCard(
                    cashOnHand = uiState.cashOnHand,
                    onCashOnHandChange = viewModel::onCashOnHandChange,
                    stockOpeningValue = uiState.stockOpeningValue,
                    onStockOpeningValueChange = viewModel::onStockOpeningValueChange,
                    cashierName = uiState.cashierName,
                    onCashierNameChange = viewModel::onCashierNameChange,
                    isLoading = uiState.isLoading,
                    onStartDay = viewModel::startDay
                )
            } else {
                DaySummaryCard(
                    onCloseDay = viewModel::closeDay,
                    isLoading = uiState.isLoading
                )
            }

            // Quick Actions
            QuickActionsCard(
                onNewInvoice = onNavigateToInvoice,
                onReports = onNavigateToReports,
                onOpenPos = onNavigateToProducts
            )

            // Products Card
            ProductsCard(
                onOpenPos = onNavigateToProducts
            )
        }
    }
}

@Composable
fun DashboardHeader(
    dayStatus: DayStatus,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "M-POS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (dayStatus == DayStatus.OPEN) "Day opened" else "Day closed",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
        IconButton(onClick = onProfileClick) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile"
            )
        }
    }
}

@Composable
fun TopQuickArea(
    dayStatus: DayStatus,
    onStartDayClick: () -> Unit,
    onSearchClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (dayStatus == DayStatus.CLOSED) {
            Button(
                onClick = onStartDayClick,
                modifier = Modifier.weight(0.4f)
            ) {
                Text("Start Day")
            }
        }
        
        var searchQuery by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.weight(0.6f),
            placeholder = { Text("Search products...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClick(searchQuery) }),
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
fun OpenDayCard(
    cashOnHand: String,
    onCashOnHandChange: (String) -> Unit,
    stockOpeningValue: String,
    onStockOpeningValueChange: (String) -> Unit,
    cashierName: String,
    onCashierNameChange: (String) -> Unit,
    isLoading: Boolean,
    onStartDay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Open Day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = cashOnHand,
                onValueChange = onCashOnHandChange,
                label = { Text("Cash on hand *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = stockOpeningValue,
                onValueChange = onStockOpeningValueChange,
                label = { Text("Stock opening value (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = cashierName,
                onValueChange = onCashierNameChange,
                label = { Text("Cashier name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { /* Save Draft */ }) {
                    Text("Save draft")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onStartDay,
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Start Day")
                    }
                }
            }
        }
    }
}

@Composable
fun DaySummaryCard(
    onCloseDay: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Day is Open",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sales: $0.00",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = onCloseDay,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text("Close Day")
                }
            }
        }
    }
}

@Composable
fun QuickActionsCard(
    onNewInvoice: () -> Unit,
    onReports: () -> Unit,
    onOpenPos: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNewInvoice,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("New Invoice")
                }
                OutlinedButton(
                    onClick = onReports,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reports")
                }
            }
            OutlinedButton(
                onClick = onOpenPos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open POS / Products")
            }
        }
    }
}

@Composable
fun ProductsCard(
    onOpenPos: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onOpenPos
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Products",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage your inventory",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go to Products")
        }
    }
}
