package com.example.panedemo.panecomparison.common.product

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<ProductUiState> =
        MutableStateFlow(ProductUiState.createInitialModel())

    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
}