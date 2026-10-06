package com.example.panedemo.panecomparison.nav3.product

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.panedemo.nav.LocalTwoPaneState
import com.example.panedemo.nav.Nav3Navigator
import com.example.panedemo.panecomparison.common.product.ProductArea
import com.example.panedemo.panecomparison.common.product.ProductContent
import com.example.panedemo.panecomparison.common.product.ProductUiState
import com.example.panedemo.panecomparison.common.product.ProductViewModel
import com.example.panedemo.panecomparison.nav3.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(navigator: Nav3Navigator) {
    val viewModel: ProductViewModel = viewModel()
    val viewState: ProductUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isTwoPane = LocalTwoPaneState.current != null

    if (isTwoPane) {
        ProductArea(
            item = viewState,
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(viewState.title, style = MaterialTheme.typography.titleSmall) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                )
            },
        ) { innerPadding ->
            ProductContent(
                viewState = viewState,
                onContactClicked = { navigator.navigate(NavRoute.Message) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}
