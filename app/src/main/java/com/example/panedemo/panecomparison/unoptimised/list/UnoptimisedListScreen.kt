package com.example.panedemo.panecomparison.unoptimised.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.panedemo.ProductItem
import com.example.panedemo.panecomparison.common.list.DEMO_LIST_ITEMS
import com.example.panedemo.panecomparison.common.list.DEMO_LIST_TOTAL
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

private const val PAGE_SIZE = 50
private const val PREFETCH_DISTANCE = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnoptimisedListScreen(
    onItemClicked: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val items = DEMO_LIST_ITEMS
    var loadedCount by rememberSaveable { mutableIntStateOf(PAGE_SIZE) }
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (lastIndex >= loadedCount - PREFETCH_DISTANCE && loadedCount < DEMO_LIST_TOTAL) {
                    loadedCount = minOf(loadedCount + PAGE_SIZE, DEMO_LIST_TOTAL)
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Real Estate", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "$loadedCount / ${DEMO_LIST_TOTAL} loaded",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(
                count = loadedCount,
                key = { items[it].id },
            ) { index ->
                ProductItem(
                    item = items[index],
                    onProductClicked = onItemClicked,
                    onWatchlistClicked = {},
                )
            }
        }
    }
}