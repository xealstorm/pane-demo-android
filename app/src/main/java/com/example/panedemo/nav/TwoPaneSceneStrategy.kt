package com.example.panedemo.nav

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.example.panedemo.nav.TwoPaneSceneStrategy.Companion.twoPane

/**
 * Renders the top two [NavEntry]s side-by-side when the top entry carries [twoPane] metadata and
 * the host signals that wide layout is available. The metadata value is the shared-toolbar title.
 *
 * One strategy, two divider modes: pass `resizable = true` for a user-draggable divider (each pane
 * clamped to [minPaneWidth]); `false` for fixed [TwoPaneMode]-derived weights. The strategy owns
 * the toolbar end-to-end — title / back / swap — so pane screens render only their body in
 * dual-pane and read [LocalTwoPaneState] to know whether to draw their single-pane fallback.
 */
class TwoPaneSceneStrategy<T : Any>(
    private val modeProvider: () -> TwoPaneMode,
    private val resizable: Boolean = false,
    private val minPaneWidth: Dp = DEFAULT_MIN_PANE_WIDTH,
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (entries.size < 2) return null
        val top = entries.last()
        val title = top.metadata[TwoPaneKey] ?: return null

        val mode = modeProvider()
        if (mode == TwoPaneMode.SINGLE_PANE) return null

        val under = entries[entries.lastIndex - 1]
        return TwoPaneScene(
            key = "two-pane:${under.contentKey}+${top.contentKey}",
            previousEntries = entries.dropLast(2),
            primary = under,
            secondary = top,
            primaryWeight = mode.primaryWeight,
            secondaryWeight = mode.secondaryWeight,
            title = title,
            onBack = onBack,
            resizable = resizable,
            minPaneWidth = minPaneWidth,
        )
    }

    object TwoPaneKey : NavMetadataKey<String>

    companion object {
        /** Marks a [NavEntry] as the secondary pane; the string is the shared-toolbar title. */
        fun twoPane(title: String) = metadata { put(TwoPaneKey, title) }
    }
}

internal data class TwoPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val primary: NavEntry<T>,
    val secondary: NavEntry<T>,
    val primaryWeight: Float,
    val secondaryWeight: Float,
    val title: String,
    val onBack: () -> Unit,
    val resizable: Boolean,
    val minPaneWidth: Dp,
) : Scene<T> {

    override val entries: List<NavEntry<T>> = listOf(primary, secondary)

    @OptIn(ExperimentalMaterial3Api::class)
    override val content: @Composable (() -> Unit) = {
        val isPanesSwapped = rememberSaveable { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isPanesSwapped.value = !isPanesSwapped.value }) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Swap panes")
                    }
                },
            )

            val leftEntry = if (isPanesSwapped.value) secondary else primary
            val rightEntry = if (isPanesSwapped.value) primary else secondary

            CompositionLocalProvider(
                LocalTwoPaneState provides TwoPaneState(isSecondaryOnLeft = isPanesSwapped.value),
            ) {
                if (resizable) {
                    ResizableRow(leftEntry, rightEntry, primaryWeight, secondaryWeight, minPaneWidth)
                } else {
                    val leftWeight = if (isPanesSwapped.value) secondaryWeight else primaryWeight
                    val rightWeight = if (isPanesSwapped.value) primaryWeight else secondaryWeight
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(Modifier.weight(leftWeight).fillMaxSize()) { leftEntry.Content() }
                        Box(Modifier.weight(rightWeight).fillMaxSize()) { rightEntry.Content() }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T : Any> ResizableRow(
    leftEntry: NavEntry<T>,
    rightEntry: NavEntry<T>,
    primaryWeight: Float,
    secondaryWeight: Float,
    minPaneWidth: Dp,
) {
    val initialRatio = primaryWeight / (primaryWeight + secondaryWeight)
    val dividerRatio = rememberSaveable { mutableFloatStateOf(initialRatio) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val totalWidthPx = constraints.maxWidth.toFloat()
        val minPaneWidthPx = with(density) { minPaneWidth.toPx() }
        val dividerWidthPx = with(density) { DIVIDER_HIT_AREA_WIDTH.toPx() }
        val usableWidthPx = totalWidthPx - dividerWidthPx
        val minRatio = minPaneWidthPx / usableWidthPx
        val maxRatio = 1f - minRatio
        val canDrag = minRatio < maxRatio

        // Pin to centre when the container is too narrow to honour minPaneWidth on both sides.
        // Writing to snapshot state during composition is a documented Compose antipattern.
        LaunchedEffect(canDrag) {
            if (!canDrag) dividerRatio.floatValue = 0.5f
        }

        val draggableState = rememberDraggableState { deltaPx ->
            dividerRatio.floatValue =
                (dividerRatio.floatValue + deltaPx / usableWidthPx).coerceIn(minRatio, maxRatio)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            val leftRatio = dividerRatio.floatValue
            Box(Modifier.weight(leftRatio).fillMaxSize()) { leftEntry.Content() }
            Box(
                Modifier
                    .width(DIVIDER_HIT_AREA_WIDTH)
                    .fillMaxHeight()
                    .then(
                        if (canDrag) {
                            Modifier
                                .draggable(orientation = Orientation.Horizontal, state = draggableState)
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else {
                            Modifier
                        },
                    ),
            ) {
                VerticalDivider(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
            Box(Modifier.weight(1f - leftRatio).fillMaxSize()) { rightEntry.Content() }
        }
    }
}

private val DIVIDER_HIT_AREA_WIDTH: Dp = 24.dp
private val DEFAULT_MIN_PANE_WIDTH: Dp = 200.dp
