package com.example.panedemo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.panedemo.ui.theme.PaneDemoTheme

data class ProductItemData(
    val id: String,
    val title: String,
    val price: String,
    val originalPrice: String? = null,
    val neighborhood: String,
    val postTime: String,
    val tags: List<String> = emptyList(),
    @DrawableRes val image: Int,
    val imageCount: String? = null,
    val topLabels: List<ProductBadge> = emptyList(),
    val bottomLabel: ProductBadge? = null,
    val companyName: String = "",
    val isWatchlisted: Boolean = false,
    val isHighlighted: Boolean = false,
    val imageAspectRatio: Float = WIDE_IMAGE_ASPECT_RATIO,
)

data class ProductBadge(
    val displayValue: String,
    @DrawableRes val iconRes: Int? = null,
    val backgroundColor: Color? = null,
    val tint: Color? = null,
)

const val WIDE_IMAGE_ASPECT_RATIO = 4f / 3f
const val POWER_WIDE_IMAGE_ASPECT_RATIO = 16f / 9f

@Composable
fun ProductItem(
    item: ProductItemData,
    onProductClicked: (String) -> Unit,
    onWatchlistClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerRadius = RoundedCornerShape(4.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .clip(cornerRadius)
            .clickable { onProductClicked(item.id) },
    ) {
        CoverImage(
            item = item,
            onWatchlistClicked = onWatchlistClicked,
            modifier = Modifier
                .aspectRatio(item.imageAspectRatio)
                .clip(
                    if (item.isHighlighted) {
                        RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                    } else {
                        RoundedCornerShape(4.dp)
                    },
                ),
        )
        InfoSection(item = item)
    }
}

@Composable
private fun CoverImage(
    item: ProductItemData,
    onWatchlistClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )
        if (item.topLabels.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                item.topLabels.forEach { label ->
                    LabelBadge(label = label)
                }
            }
        }
        IconButton(
            onClick = { onWatchlistClicked(item.id) },
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = if (item.isWatchlisted) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Watchlist",
                tint = if (item.isWatchlisted) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        }
        if (item.imageCount != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text(
                    text = item.imageCount,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun InfoSection(item: ProductItemData) {
    val background = if (item.isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            HeaderText(
                text = item.neighborhood,
                textAlign = TextAlign.Start,
                isHighlighted = item.isHighlighted,
                modifier = Modifier.weight(1f),
            )
            HeaderText(
                text = item.postTime,
                textAlign = TextAlign.End,
                isHighlighted = item.isHighlighted,
            )
        }
        TitleText(text = item.title, isHighlighted = item.isHighlighted)
        if (item.tags.isNotEmpty()) {
            Text(
                text = item.tags.joinToString(separator = " · "),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = contentColor(item.isHighlighted),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        PriceText(
            price = item.price,
            originalPrice = item.originalPrice,
            isHighlighted = item.isHighlighted,
        )
        if (item.bottomLabel != null) {
            LabelBadge(label = item.bottomLabel)
        }
        if (item.companyName.isNotEmpty()) {
            Text(
                text = item.companyName,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor(item.isHighlighted).copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun HeaderText(
    text: String,
    textAlign: TextAlign,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = contentColor(isHighlighted).copy(alpha = 0.7f),
        textAlign = textAlign,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Composable
private fun TitleText(text: String, isHighlighted: Boolean) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = contentColor(isHighlighted),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun PriceText(
    price: String,
    originalPrice: String?,
    isHighlighted: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor(isHighlighted),
        )
        if (!originalPrice.isNullOrEmpty()) {
            Text(
                text = originalPrice,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor(isHighlighted).copy(alpha = 0.6f),
                textDecoration = TextDecoration.LineThrough,
            )
        }
    }
}

@Composable
private fun LabelBadge(label: ProductBadge) {
    val background = label.backgroundColor ?: MaterialTheme.colorScheme.surfaceVariant
    val foreground = label.tint ?: MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .background(color = background, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (label.iconRes != null) {
            Icon(
                painter = painterResource(id = label.iconRes),
                contentDescription = null,
                tint = foreground,
                modifier = Modifier.size(14.dp),
            )
        }
        Text(
            text = label.displayValue,
            style = MaterialTheme.typography.labelSmall,
            color = foreground,
        )
    }
}

@Composable
private fun contentColor(isHighlighted: Boolean): Color =
    if (isHighlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

@Preview(showBackground = true, name = "Standard")
@Composable
private fun PreviewProductItemStandard() {
    PaneDemoTheme {
        ProductItem(
            item = ProductItemData(
                id = "1",
                title = "Spacious 3-room apartment in Hellschen-Heringsand-Unterschaar",
                price = "€674,387",
                originalPrice = "€785,498",
                neighborhood = "Hellschen-Heringsand-Unterschaar (4500km)",
                postTime = "Yesterday 13:45",
                tags = listOf("102 m²", "3 rooms"),
                image = R.drawable.demo_apartment_1,
                imageCount = "5",
                topLabels = emptyList(),
                bottomLabel = ProductBadge(displayValue = "Buy Direct"),
                companyName = "Immobilien Müller GmbH",
            ),
            onProductClicked = {},
            onWatchlistClicked = {},
        )
    }
}

@Preview(showBackground = true, name = "Highlighted")
@Composable
private fun PreviewProductItemHighlighted() {
    PaneDemoTheme {
        ProductItem(
            item = ProductItemData(
                id = "2",
                title = "Rented and well-maintained 3-room apartment in Friedrichsfelde",
                price = "€674,387",
                originalPrice = "€785,498",
                neighborhood = "Hellschen-Heringsand-Unterschaar (4500km)",
                postTime = "Yesterday 13:45",
                tags = listOf("102 m²", "3 rooms"),
                image = R.drawable.demo_apartment_2,
                imageCount = "5",
                topLabels = listOf(
                    ProductBadge(displayValue = "360° Tour"),
                    ProductBadge(displayValue = "Floor plan"),
                ),
                companyName = "Immobilien Müller GmbH",
                isHighlighted = true,
            ),
            onProductClicked = {},
            onWatchlistClicked = {},
        )
    }
}

@Preview(showBackground = true, name = "Top ad")
@Composable
private fun PreviewProductItemTopAd() {
    PaneDemoTheme {
        ProductItem(
            item = ProductItemData(
                id = "3",
                title = "Rented and well-maintained 3-room apartment in Friedrichsfelde",
                price = "€123,456 – €674,387",
                neighborhood = "Hellschen-Heringsand-Unterschaar (4500km)",
                postTime = "Yesterday 13:45",
                tags = listOf("55 – 102 m²", "2 – 3 rooms"),
                image = R.drawable.demo_apartment_3,
                imageCount = "5",
                topLabels = listOf(
                    ProductBadge(
                        displayValue = "TOP",
                        backgroundColor = Color(0xFF1E88E5),
                        tint = Color.White,
                    ),
                    ProductBadge(displayValue = "360° Tour"),
                    ProductBadge(displayValue = "Floor plan"),
                ),
                companyName = "Immobilien Müller GmbH",
                imageAspectRatio = POWER_WIDE_IMAGE_ASPECT_RATIO,
                isWatchlisted = true,
            ),
            onProductClicked = {},
            onWatchlistClicked = {},
        )
    }
}