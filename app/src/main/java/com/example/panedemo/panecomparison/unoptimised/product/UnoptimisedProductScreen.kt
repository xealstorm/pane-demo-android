package com.example.panedemo.panecomparison.unoptimised.product

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.panedemo.panecomparison.common.product.ProductUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnoptimisedProductScreen(
    viewState: ProductUiState,
    onContactClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewState.title, style = MaterialTheme.typography.titleSmall) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        UnoptimisedProductContent(
            viewState = viewState,
            onContactClicked = onContactClicked,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}