package com.example.panedemo.panecomparison.common.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DemoSelectorBottomSheet(
    openSelector: DemoSelectorSpec<*>?,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var lastOpenSelector by remember { mutableStateOf<DemoSelectorSpec<*>?>(null) }

    LaunchedEffect(openSelector) {
        if (openSelector != null) lastOpenSelector = openSelector
    }

    val displayed = openSelector ?: lastOpenSelector.takeIf { sheetState.isVisible }

    if (openSelector != null || sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
        ) {
            Text(
                text = displayed?.label.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            displayed?.let { spec ->
                DemoSelectorSheetContent(
                    spec = spec,
                    onSelected = {
                        scope.launch {
                            sheetState.hide()
                            onDismiss()
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun DemoSelectorSheetContent(
    spec: DemoSelectorSpec<*>,
    onSelected: () -> Unit,
) {
    Column {
        spec.displayOptions.forEach { option ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        option.onSelect()
                        onSelected()
                    }
                    .padding(12.dp),
            ) {
                Text(option.label, style = MaterialTheme.typography.bodyMedium)
            }
            HorizontalDivider()
        }
    }
}
