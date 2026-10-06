package com.example.panedemo.nav

enum class TwoPaneMode(val primaryWeight: Float, val secondaryWeight: Float) {
    SINGLE_PANE(primaryWeight = 1f, secondaryWeight = 0f),
    FOLDABLE(primaryWeight = 1f, secondaryWeight = 1f),
    WIDE(primaryWeight = 2f, secondaryWeight = 3f),
}