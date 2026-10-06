package com.example.panedemo.panecomparison.common.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
internal fun <T : Enum<T>> SelectorInputField(
    label: String,
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    optionLabel: (T) -> String,
    isSecondPaneOpen: Boolean,
    onOpenSelector: (DemoSelectorSpec<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isSecondPaneOpen) {
        DemoEnumDropdownPopup(
            label = label,
            options = options,
            selected = selected,
            onSelect = onSelect,
            optionLabel = optionLabel,
            modifier = modifier,
        )
    } else {
        DemoEnumBottomSheetTrigger(
            label = label,
            selected = selected,
            optionLabel = optionLabel,
            onClick = {
                onOpenSelector(
                    DemoSelectorSpec(
                        label = label,
                        options = options,
                        optionLabel = optionLabel,
                        onSelect = onSelect,
                    ),
                )
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun <T : Enum<T>> DemoEnumDropdownPopup(
    label: String,
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val maxDropdownHeight = with(density) { (windowInfo.containerSize.height / 2).toDp() }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = optionLabel(selected),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { isExpanded = true },
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.heightIn(max = maxDropdownHeight),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option), style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        onSelect(option)
                        isExpanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun <T : Enum<T>> DemoEnumBottomSheetTrigger(
    label: String,
    selected: T,
    optionLabel: (T) -> String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = optionLabel(selected),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onClick() },
        )
    }
}

internal class DemoSelectorSpec<T : Enum<T>>(
    val label: String,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
) {
    val displayOptions: List<DisplayOption> = options.map { option ->
        DisplayOption(
            label = optionLabel(option),
            onSelect = { onSelect(option) },
        )
    }

    data class DisplayOption(
        val label: String,
        val onSelect: () -> Unit,
    )
}
