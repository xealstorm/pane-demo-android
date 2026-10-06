package com.example.panedemo.panecomparison.legacy.message

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.panedemo.panecomparison.common.product.ProductUiState
import com.example.panedemo.panecomparison.common.product.ProductArea

@Composable
fun MessageDetailsPane(
    viewState: ProductUiState,
    modifier: Modifier = Modifier,
) {
    ProductArea(
        item = viewState,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    )
}
