package com.example.panedemo.panecomparison.legacy.message

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.panedemo.panecomparison.common.message.MessageContent
import com.example.panedemo.panecomparison.common.message.MessageSendButton
import com.example.panedemo.panecomparison.common.message.MessageUiState
import com.example.panedemo.panecomparison.common.message.MessageViewModel
import com.example.panedemo.panecomparison.common.product.ProductUiState
import com.example.panedemo.panecomparison.common.message.rememberIsNarrowPane

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MessageScreen(
    viewState: MessageUiState,
    productViewState: ProductUiState,
    viewModel: MessageViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPanesSwapped by rememberSaveable { mutableStateOf(false) }
    val activity = LocalActivity.current
    val windowSizeClass = activity?.let { calculateWindowSizeClass(it) }

    BoxWithConstraints(modifier = modifier) {
        val isTwoPaneLayout =
            maxWidth >= TWO_PANE_MIN_WIDTH && windowSizeClass?.heightSizeClass != WindowHeightSizeClass.Compact

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Write reply", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        if (isTwoPaneLayout) {
                            IconButton(onClick = { isPanesSwapped = !isPanesSwapped }) {
                                Icon(Icons.Default.SwapHoriz, contentDescription = "Swap panes")
                            }
                        }
                    },
                )
            },
        ) { innerPadding ->
            if (isTwoPaneLayout) {
                MessageDualPaneContent(
                    viewState = viewState,
                    productViewState = productViewState,
                    viewModel = viewModel,
                    isPanesSwapped = isPanesSwapped,
                    modifier = Modifier.padding(innerPadding),
                )
            } else {
                SinglePaneForm(
                    viewState = viewState,
                    viewModel = viewModel,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun SinglePaneForm(
    viewState: MessageUiState,
    viewModel: MessageViewModel,
    modifier: Modifier = Modifier,
) {
    val isNarrowPane = rememberIsNarrowPane(isSecondPaneOpen = false)
    Column(modifier = modifier) {
        MessageContent(
            uiState = viewState,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
            isSecondPaneOpen = false,
            isNarrowPane = isNarrowPane,
        )
        MessageSendButton(
            onClick = viewModel::onSendClicked,
            isSecondPaneOpen = false,
        )
    }
}

private val TWO_PANE_MIN_WIDTH = 600.dp
