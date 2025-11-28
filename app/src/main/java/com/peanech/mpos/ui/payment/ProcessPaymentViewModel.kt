package com.peanech.mpos.ui.payment

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.peanech.mpos.core.data.repository.CartRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProcessPaymentViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _paymentMethod = MutableStateFlow("Cash")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    // Cash State
    private val _cashReceived = MutableStateFlow("")
    val cashReceived: StateFlow<String> = _cashReceived.asStateFlow()

    private val _change = MutableStateFlow(0.0)
    val change: StateFlow<Double> = _change.asStateFlow()

    // QR State
    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
    val qrCodeBitmap: StateFlow<Bitmap?> = _qrCodeBitmap.asStateFlow()

    // Payment Status
    private val _paymentStatus = MutableStateFlow<PaymentStatus>(PaymentStatus.Idle)
    val paymentStatus: StateFlow<PaymentStatus> = _paymentStatus.asStateFlow()

    private val _currentOrderId = MutableStateFlow("")
    val currentOrderId: StateFlow<String> = _currentOrderId.asStateFlow()

    init {
        _currentOrderId.value = "ORD-${System.currentTimeMillis() / 1000}"
        val passedTotalString = savedStateHandle.get<String>("totalAmount")
        val passedTotal = passedTotalString?.toDoubleOrNull()

        if (passedTotal != null && passedTotal > 0) {
            _totalAmount.value = passedTotal
        } else {
            viewModelScope.launch {
                cartRepository.getCartItems().collect { items ->
                    val sub = items.sumOf { it.product.price * it.quantity }
                    val tx = sub * 0.1
                    _totalAmount.value = sub + tx
                }
            }
        }
    }

    fun selectPaymentMethod(method: String) {
        _paymentMethod.value = method
        if (method == "QR Code") {
            generateQrCode("ORDER-12345-TOTAL-${_totalAmount.value}")
        }
    }

    fun onCashReceivedChanged(amount: String) {
        _cashReceived.value = amount
        val received = amount.toDoubleOrNull() ?: 0.0
        _change.value = if (received >= _totalAmount.value) received - _totalAmount.value else 0.0
    }

    private fun generateQrCode(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val width = 512
                val height = 512
                val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
                )
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                withContext(Dispatchers.Main) {
                    _qrCodeBitmap.value = bitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun processPayment() {
        _paymentStatus.value = PaymentStatus.Processing
        viewModelScope.launch {
            // Simulate network delay
            kotlinx.coroutines.delay(2000)
            _paymentStatus.value = PaymentStatus.Success
            // Cart is NOT cleared here anymore. It will be cleared when starting a new sale.
        }
    }
}

sealed class PaymentStatus {
    object Idle : PaymentStatus()
    object Processing : PaymentStatus()
    object Success : PaymentStatus()
    data class Failure(val message: String) : PaymentStatus()
}
