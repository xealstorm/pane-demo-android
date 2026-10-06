package com.example.panedemo.panecomparison.common.product

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.panedemo.R

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProductContent(
    viewState: ProductUiState,
    onContactClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val activity = LocalActivity.current ?: return
    val windowSizeClass = calculateWindowSizeClass(activity)

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        ProductArea(item = viewState)
        HorizontalDivider()

        val alignmentModifier =
            if (windowSizeClass.widthSizeClass >=WindowWidthSizeClass.Medium && windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
            Modifier.align(Alignment.End)
        } else {
            Modifier.fillMaxWidth()
        }
        Button(
            onClick = onContactClicked,
            modifier = alignmentModifier
                .padding(16.dp),
        ) {
            Text("Contact Seller")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ProductArea(
    item: ProductUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val imageHeight = maxWidth * 3f / 4f
            if (imageHeight <= 350.dp) {
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
            } else {
                LazyRow (
                    modifier = Modifier.height(350.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(item.pictures, key = { it }) { picture ->
                        Image(
                            painter = painterResource(id = picture),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(DEMO_HERO_ASPECT_RATIO),
                        )
                    }
                }
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
        FlowRow(
//            maxItemsInEachRow = 3,
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
//                softWrap = false,
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
//        FlowRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(
//                    horizontal = 8.dp,
//                    vertical = 8.dp
//                ),
////            maxItemsInEachRow = 4,
//        ) {
//            item.features.forEach { feature ->
//                LabeledText(
//                    text = feature,
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_check),
//                            tint = MaterialTheme.colorScheme.primary,
//                            contentDescription = "Check Icon",
//                        )
//                    },
//                    modifier = Modifier
////                        .weight(1f)
//                        .padding(
//                            horizontal = 8.dp,
//                            vertical = 8.dp
//                        )
//                )
//            }
//        }
        val featureTextStyle = MaterialTheme.typography.bodyMedium
        val textMeasurer = rememberTextMeasurer()
        val density = LocalDensity.current
        val longestFeatureTextWidth = remember(item.features, featureTextStyle, density) {
            val maxPx = item.features.maxOfOrNull { feature ->
                textMeasurer.measure(text = feature, style = featureTextStyle).size.width
            } ?: 0
            with(density) { maxPx.toDp() }
        }
        // Icon default 24dp + 8dp icon-to-text spacing (LabeledText) + 16dp item horizontal padding
        val minItemWidth = longestFeatureTextWidth + 24.dp + 8.dp + 16.dp
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            val featureIcon: @Composable () -> Unit = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Check Icon",
                )
            }
            if (maxWidth < minItemWidth) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    item.features.forEach { feature ->
                        LabeledText(
                            text = feature,
                            icon = featureIcon,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 8.dp
                                ),
                            softWrap = true,
                        )
                    }
                }
            } else {
                val fitting = (maxWidth / minItemWidth).toInt()
                val columns = fitting.coerceAtMost(item.features.size.coerceAtLeast(1))
                Column(modifier = Modifier.fillMaxWidth()) {
                    item.features.chunked(columns).forEach { rowItems ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            rowItems.forEach { feature ->
                                LabeledText(
                                    text = feature,
                                    icon = featureIcon,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(
                                            horizontal = 8.dp,
                                            vertical = 8.dp
                                        ),
                                    softWrap = false,
                                )
                            }
                            repeat(columns - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
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
}

@Composable
fun ContentTitleWithTopGap(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                top = 20.dp
            )
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.background
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.background,
            thickness = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                )
        )
    }
}

@Composable
fun LabeledText(
    text: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    softWrap: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            softWrap = softWrap,
            modifier = Modifier.padding(
                start = 8.dp,
            )
        )
    }
}

private const val DEMO_HERO_ASPECT_RATIO = 4f / 3f