package com.example.panedemo.panecomparison.unoptimised.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.panedemo.R
import com.example.panedemo.panecomparison.common.product.ContentTitleWithTopGap
import com.example.panedemo.panecomparison.common.product.LabeledText
import com.example.panedemo.panecomparison.common.product.ProductUiState

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun UnoptimisedProductContent(
    viewState: ProductUiState,
    onContactClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        UnoptimisedProductArea(item = viewState)
        HorizontalDivider()

        Button(
            onClick = onContactClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text("Contact Seller")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun UnoptimisedProductArea(
    item: ProductUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState(pageCount = { item.pictures.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 16.dp,
        ) { page ->
            Image(
                painter = painterResource(id = item.pictures[page]),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(DEMO_HERO_ASPECT_RATIO),
            )
        }
    }
    Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(
            16.dp,
            20.dp,
            16.dp,
        )
    )
    item.priceWithCurrency?.let { text ->
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    16.dp,
                )
        )
    }
    Text(
        text = item.address ?: "(${item.location?.latitude}, ${item.location?.longitude})",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .combinedClickable(
                onClick = {
                },
                onLongClick = {
                }
            )
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LabeledText(
            text = item.postedDate,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Calendar Icon"
                )
            },
            modifier = Modifier.padding(
                top = 8.dp,
            ),
        )
        LabeledText(
            text = item.visits,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_visits),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Eye Icon"
                )
            },
            modifier = Modifier.padding(
                top = 8.dp,
            ),
        )
        Text(
            text = "ID: ${item.id}",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = 8.dp,
                ),
        )
    }
    ContentTitleWithTopGap(
        title = "Features",
        modifier = Modifier.fillMaxWidth(),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
    ) {
        item.features.forEach { feature ->
            LabeledText(
                text = feature,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Check Icon",
                    )
                },
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
            )
        }
    }
    ContentTitleWithTopGap(
        title = "Description",
        modifier = Modifier.fillMaxWidth(),
    )
    Text(
        text = item.description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(16.dp)
    )
}

private const val DEMO_HERO_ASPECT_RATIO = 4f / 3f