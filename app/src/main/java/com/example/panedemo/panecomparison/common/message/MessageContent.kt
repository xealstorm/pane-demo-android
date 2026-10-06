package com.example.panedemo.panecomparison.common.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val SPACING_MEDIUM_DP = 12.dp

@Composable
fun MessageContent(
    uiState: MessageUiState,
    viewModel: MessageViewModel,
    modifier: Modifier = Modifier,
    isSecondPaneOpen: Boolean = false,
    isNarrowPane: Boolean = rememberIsNarrowPane(isSecondPaneOpen = isSecondPaneOpen),
) {
    val horizontalPadding = if (isSecondPaneOpen) 16.dp else SPACING_MEDIUM_DP
    var openSelector by remember { mutableStateOf<DemoSelectorSpec<*>?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
    ) {
        MessageGroup(
            uiState = uiState,
            viewModel = viewModel,
            horizontalPadding = horizontalPadding,
        )
        HorizontalDivider()
        ContactGroup(
            uiState = uiState,
            viewModel = viewModel,
            horizontalPadding = horizontalPadding,
            isSecondPaneOpen = isSecondPaneOpen,
            isNarrowPane = isNarrowPane,
            onOpenSelector = { openSelector = it },
        )
        HorizontalDivider()
        InformationGroup(
            uiState = uiState,
            viewModel = viewModel,
            horizontalPadding = horizontalPadding,
            isSecondPaneOpen = isSecondPaneOpen,
            isNarrowPane = isNarrowPane,
            onOpenSelector = { openSelector = it },
        )
    }

    if (!isSecondPaneOpen) {
        DemoSelectorBottomSheet(
            openSelector = openSelector,
            onDismiss = { openSelector = null },
        )
    }
}

@Composable
internal fun ColumnScope.MessageSendButton(
    onClick: () -> Unit,
    isSecondPaneOpen: Boolean,
    isFormOnLeft: Boolean = false,
) {
    val horizontalPadding = if (isSecondPaneOpen) 16.dp else SPACING_MEDIUM_DP
    val alignmentModifier = when {
        !isSecondPaneOpen -> Modifier.fillMaxWidth()
        isFormOnLeft -> Modifier.align(Alignment.Start)
        else -> Modifier.align(Alignment.End)
    }
    Button(
        onClick = onClick,
        modifier = alignmentModifier.padding(
            horizontal = horizontalPadding,
            vertical = SPACING_MEDIUM_DP,
        ),
    ) {
        Text(SEND_BUTTON_TEXT)
    }
}

/**
 * Approximates whether the pane rendering the form is too narrow to lay two fields out side-by-side.
 * Backed by [LocalWindowInfo] rather than a [BoxWithConstraints]
 * per row — avoids a subcompose pass on every divider-drag frame in the resizable demo.
 */
@Composable
internal fun rememberIsNarrowPane(isSecondPaneOpen: Boolean): Boolean {
    val containerSize = LocalWindowInfo.current.containerSize
    val windowWidthDp = with(LocalDensity.current) { containerSize.width.toDp() }
    val approxPaneWidth = if (isSecondPaneOpen) windowWidthDp / 2 else windowWidthDp
    return approxPaneWidth < NARROW_PANE_THRESHOLD_DP
}

@Composable
internal fun AdaptiveFieldRow(
    isSecondPaneOpen: Boolean,
    isNarrowPane: Boolean,
    fields: @Composable (fieldModifier: Modifier) -> Unit,
) {
    if (isSecondPaneOpen && !isNarrowPane) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fields(Modifier.weight(1f))
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            fields(Modifier.fillMaxWidth())
        }
    }
}

/**
 * Three-state sibling of [AdaptiveFieldRow] for a lead field plus two trailing fields.
 * Measures the real available width (not an approximation from [LocalWindowInfo]) so it reacts
 * correctly as the resizable pane divider is dragged, where screen-width halving misreports
 * the pane width.
 *
 * At the all-in-row breakpoint, the lead renders at `leadMinWidth` and each tail at `tailMinWidth`
 * under 1:2:2 weights; below it, the lead moves to its own half-width line; below the tail-row
 * breakpoint, everything stacks.
 */
@Composable
internal fun AdaptiveFieldRowWithLead(
    lead: @Composable (fieldModifier: Modifier) -> Unit,
    fields: @Composable (fieldModifier: Modifier) -> Unit,
    isNarrowPane: Boolean = true,
    leadMinWidth: Dp = 180.dp,
    tailMinWidth: Dp = 200.dp,
) {
    val gap = 8.dp
    BoxWithConstraints {
        val available = maxWidth
        val allInRowMin = maxOf(leadMinWidth * 4, tailMinWidth * 2) + gap * 2
        val leadAboveMin = tailMinWidth * 2 + gap
        when {
            available >= allInRowMin && !isNarrowPane -> {
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    lead(Modifier.weight(1.5f))
                    fields(Modifier.weight(2f))
                }
            }
            available >= leadAboveMin && !isNarrowPane -> {
                Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                    lead(Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                        fields(Modifier.weight(1f))
                    }
                }
            }
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                    lead(Modifier.fillMaxWidth())
                    fields(Modifier.fillMaxWidth())
                }
            }
        }
    }
}
private val NARROW_PANE_THRESHOLD_DP = 408.dp
