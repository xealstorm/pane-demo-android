package com.example.panedemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoLauncherScreen(
    onUnoptimisedClicked: () -> Unit,
    onLegacyDemoClicked: () -> Unit,
    onNav3DemoClicked: () -> Unit,
    onResizableNav3DemoClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dual-pane demos", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            DemoRow(
                title = "Demo 0: Unoptimised",
                subtitle = "",
                onClick = onUnoptimisedClicked,
            )
            HorizontalDivider()
            DemoRow(
                title = "Demo 1: List → Product → Message (layout-driven)",
                subtitle = "Single composable that forks single-pane vs dual-pane via BoxWithConstraints. " +
                    "One back-stack entry, one ViewModel graph.",
                onClick = onLegacyDemoClicked,
            )
            HorizontalDivider()
            DemoRow(
                title = "Demo 2: List → Product → Message (Nav3-driven)",
                subtitle = "Same screens as Demo 1, but routed through Nav3 + TwoPaneSceneStrategy " +
                    "instead of BoxWithConstraints.",
                onClick = onNav3DemoClicked,
            )
            HorizontalDivider()
            DemoRow(
                title = "Demo 3: List → Product → Message (Nav3-driven, resizable)",
                subtitle = "Same as Demo 2, but with a draggable divider between panes. " +
                    "Each pane has a 250dp minimum width.",
                onClick = onResizableNav3DemoClicked,
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun DemoRow(title: String, subtitle: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
