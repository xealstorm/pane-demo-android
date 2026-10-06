package com.example.panedemo.panecomparison.nav3.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.panedemo.nav.LocalTwoPaneState
import com.example.panedemo.nav.Nav3Navigator
import com.example.panedemo.panecomparison.common.message.MessageContent
import com.example.panedemo.panecomparison.common.message.MessageSendButton
import com.example.panedemo.panecomparison.common.message.MessageUiState
import com.example.panedemo.panecomparison.common.message.MessageViewModel
import com.example.panedemo.panecomparison.common.message.rememberIsNarrowPane

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(navigator: Nav3Navigator) {
    val viewModel: MessageViewModel = viewModel()
    val uiState: MessageUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val twoPane = LocalTwoPaneState.current

    if (twoPane != null) {
        MessageFormWithSendButton(
            uiState = uiState,
            viewModel = viewModel,
            isSecondPaneOpen = true,
            isFormOnLeft = twoPane.isSecondaryOnLeft,
            modifier = Modifier.fillMaxHeight(),
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Write reply", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                )
            },
        ) { innerPadding ->
            MessageFormWithSendButton(
                uiState = uiState,
                viewModel = viewModel,
                isSecondPaneOpen = false,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun MessageFormWithSendButton(
    uiState: MessageUiState,
    viewModel: MessageViewModel,
    isSecondPaneOpen: Boolean,
    modifier: Modifier = Modifier,
    isFormOnLeft: Boolean = false,
) {
    val isNarrowPane = rememberIsNarrowPane(isSecondPaneOpen = isSecondPaneOpen)
    Column(modifier = modifier) {
        MessageContent(
            uiState = uiState,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
            isSecondPaneOpen = isSecondPaneOpen,
            isNarrowPane = isNarrowPane,
        )
        MessageSendButton(
            onClick = viewModel::onSendClicked,
            isSecondPaneOpen = isSecondPaneOpen,
            isFormOnLeft = isFormOnLeft,
        )
    }
}
