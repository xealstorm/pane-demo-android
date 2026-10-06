package com.example.panedemo.nav

import androidx.compose.runtime.compositionLocalOf

/**
 * Non-null means the surrounding scene is rendering two panes side-by-side. Pane screens read this
 * to decide whether to render their own [androidx.compose.material3.Scaffold] + toolbar (single-pane
 * fallback) or just their body (dual-pane; the scene owns the shared toolbar).
 */
data class TwoPaneState(val isSecondaryOnLeft: Boolean)

val LocalTwoPaneState = compositionLocalOf<TwoPaneState?> { null }
