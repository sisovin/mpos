package com.peanech.mpos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DayStatus {
    CLOSED, OPEN
}

data class DashboardUiState(
    val dayStatus: DayStatus = DayStatus.CLOSED,
    val isLoading: Boolean = false,
    val error: String? = null,
    val cashOnHand: String = "",
    val stockOpeningValue: String = "",
    val cashierName: String = ""
)

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun onCashOnHandChange(value: String) {
        _uiState.update { it.copy(cashOnHand = value) }
    }

    fun onStockOpeningValueChange(value: String) {
        _uiState.update { it.copy(stockOpeningValue = value) }
    }

    fun onCashierNameChange(value: String) {
        _uiState.update { it.copy(cashierName = value) }
    }

    fun startDay() {
        val currentState = _uiState.value
        if (currentState.cashOnHand.isBlank() || currentState.cashOnHand.toDoubleOrNull() == null) {
            _uiState.update { it.copy(error = "Invalid Cash on Hand") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Simulate API call
            delay(2000)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    dayStatus = DayStatus.OPEN
                )
            }
        }
    }

    fun closeDay() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    dayStatus = DayStatus.CLOSED,
                    cashOnHand = "",
                    stockOpeningValue = "",
                    cashierName = ""
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
