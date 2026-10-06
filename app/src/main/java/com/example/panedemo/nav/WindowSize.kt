package com.example.panedemo.nav

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun rememberIsExpandedWidth(): Boolean {
    val activity = LocalActivity.current ?: return false
    return calculateWindowSizeClass(activity).widthSizeClass == WindowWidthSizeClass.Expanded
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun rememberIsFoldableInnerScreen(): Boolean {
    val activity = LocalActivity.current ?: return false

    if (calculateWindowSizeClass(activity).widthSizeClass == WindowWidthSizeClass.Compact) return false

    val hasHinge = remember(activity) {
        activity.packageManager.hasSystemFeature(FEATURE_SENSOR_HINGE)
    }

    val hasFoldingFeature: State<Boolean?> = produceState(
        initialValue = null,
        key1 = activity,
    ) {
        try {
            WindowInfoTracker.getOrCreate(activity)
                .windowLayoutInfo(activity)
                .collect { info ->
                    value = info.displayFeatures
                        .filterIsInstance<FoldingFeature>()
                        .isNotEmpty()
                }
        } catch (_: Exception) {
            value = false
        }
    }

    return hasFoldingFeature.value == true || hasHinge
}

/**
 * Composable mode used by [TwoPaneSceneStrategy] to choose pane weights at registration time.
 *
 * Foldable inner-screen takes precedence over expanded-width because foldables can report a width
 * class smaller than [WindowWidthSizeClass.Expanded] while still wanting a 50/50 split.
 */
@Composable
fun rememberTwoPaneMode(): TwoPaneMode {
    val foldable = rememberIsFoldableInnerScreen()
    val wide = rememberIsExpandedWidth()
    return when {
        foldable -> TwoPaneMode.FOLDABLE
        wide -> TwoPaneMode.WIDE
        else -> TwoPaneMode.SINGLE_PANE
    }
}

private const val FEATURE_SENSOR_HINGE = "android.hardware.sensor.hinge_angle"
