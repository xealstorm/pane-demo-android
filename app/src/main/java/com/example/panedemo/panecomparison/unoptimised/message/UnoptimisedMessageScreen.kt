package com.example.panedemo.panecomparison.unoptimised.message

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.panedemo.panecomparison.common.message.MessageContent
import com.example.panedemo.panecomparison.common.message.MessageSendButton
import com.example.panedemo.panecomparison.common.message.MessageUiState
import com.example.panedemo.panecomparison.common.message.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun UnoptimisedMessageScreen(
    viewState: MessageUiState,
    viewModel: MessageViewModel,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write reply", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        SinglePaneForm(
            viewState = viewState,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun SinglePaneForm(
    viewState: MessageUiState,
    viewModel: MessageViewModel,
    modifier: Modifier = Modifier,
) {
    val isNarrowPane = true
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