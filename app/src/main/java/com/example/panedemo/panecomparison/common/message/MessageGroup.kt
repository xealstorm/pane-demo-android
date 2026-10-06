package com.example.panedemo.panecomparison.common.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun MessageGroup(
    uiState: MessageUiState,
    viewModel: MessageViewModel,
    horizontalPadding: Dp,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = horizontalPadding,
            vertical = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = GROUP_LABEL_MESSAGE, style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = uiState.message,
            onValueChange = viewModel::onMessageChanged,
            label = { Text(FIELD_LABEL_MESSAGE) },
            minLines = MESSAGE_MIN_LINES,
            maxLines = MESSAGE_MAX_LINES,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private const val MESSAGE_MIN_LINES = 3
private const val MESSAGE_MAX_LINES = 6
