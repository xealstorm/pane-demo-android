package com.example.panedemo.panecomparison.legacy.message

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.example.panedemo.nav.rememberIsFoldableInnerScreen
import com.example.panedemo.panecomparison.common.message.MessageContent
import com.example.panedemo.panecomparison.common.message.MessageSendButton
import com.example.panedemo.panecomparison.common.message.MessageUiState
import com.example.panedemo.panecomparison.common.message.MessageViewModel
import com.example.panedemo.panecomparison.common.product.ProductUiState
import com.example.panedemo.panecomparison.common.message.rememberIsNarrowPane

@Composable
fun MessageDualPaneContent(
    viewState: MessageUiState,
    productViewState: ProductUiState,
    viewModel: MessageViewModel,
    isPanesSwapped: Boolean,
    modifier: Modifier = Modifier,
) {
    val isFoldable = rememberIsFoldableInnerScreen()
    val currentViewState by rememberUpdatedState(viewState)
    val currentProductViewState by rememberUpdatedState(productViewState)
    val currentViewModel by rememberUpdatedState(viewModel)
    val isNarrowPane = rememberIsNarrowPane(isSecondPaneOpen = true)

    val detailPaneContent = remember {
        movableContentOf { paneModifier: Modifier ->
            MessageDetailsPane(
                viewState = currentProductViewState,
                modifier = paneModifier,
            )
        }
    }
    val formPaneContent = remember {
        movableContentOf { paneModifier: Modifier ->
            Column(modifier = paneModifier.fillMaxSize()) {
                MessageContent(
                    uiState = currentViewState,
                    viewModel = currentViewModel,
                    modifier = Modifier.weight(1f),
                    isSecondPaneOpen = true,
                    isNarrowPane = isNarrowPane,
                )
                MessageSendButton(
                    onClick = { currentViewModel.onSendClicked() },
                    isSecondPaneOpen = true,
                    isFormOnLeft = isPanesSwapped,
                )
            }
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val aspectRatio = maxWidth / maxHeight
        val useEqualWeights = isFoldable || aspectRatio in NEARLY_SQUARE_MIN_RATIO..NEARLY_SQUARE_MAX_RATIO
        val detailWeight = if (useEqualWeights) 1f else DETAIL_WEIGHT_WIDE
        val formWeight = if (useEqualWeights) 1f else FORM_WEIGHT_WIDE
        val leftWeight = if (isPanesSwapped) formWeight else detailWeight
        val rightWeight = if (isPanesSwapped) detailWeight else formWeight

        Row {
            val leftModifier = Modifier
                .weight(leftWeight)
                .fillMaxHeight()
            val rightModifier = Modifier
                .weight(rightWeight)
                .fillMaxHeight()

            if (isPanesSwapped) {
                formPaneContent(leftModifier)
                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                detailPaneContent(rightModifier)
            } else {
                detailPaneContent(leftModifier)
                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                formPaneContent(rightModifier)
            }
        }
    }
}

private const val DETAIL_WEIGHT_WIDE = 2f
private const val FORM_WEIGHT_WIDE = 3f
private const val NEARLY_SQUARE_MIN_RATIO = 0.8f
private const val NEARLY_SQUARE_MAX_RATIO = 1.25f
