package com.example.panedemo.panecomparison.common.list

import com.example.panedemo.ProductItemData
import com.example.panedemo.R
import kotlin.random.Random

private val DEMO_IMAGES = listOf(
    R.drawable.demo_apartment_1,
    R.drawable.demo_apartment_2,
    R.drawable.demo_apartment_3,
    R.drawable.demo_apartment_4,
    R.drawable.demo_apartment_5,
)

private val DEMO_TITLES = listOf(
    "Comfortable flat in Berlin",
    "Spacious penthouse in Spandau",
    "Cozy studio in Mitte",
    "Modern loft in Kreuzberg",
    "Elegant villa in Charlottenburg",
    "Charming townhouse in Prenzlauer Berg",
    "Sunny apartment in Friedrichshain",
    "Family home in Zehlendorf",
    "Renovated flat in Neukölln",
    "Riverfront studio in Treptow",
    "Bright maisonette in Wilmersdorf",
    "Classic altbau in Schöneberg",
)

private val DEMO_PRICES = listOf(
    "€300,000",
    "€375,000",
    "€450,000",
    "€525,000",
    "€625,000",
    "€750,000",
    "€895,000",
    "€1,100,000",
)

const val DEMO_LIST_TOTAL = 300

val r = Random.Default

val DEMO_LIST_ITEMS: List<ProductItemData> = List(DEMO_LIST_TOTAL) { index ->
    ProductItemData(
        id = index.toString(),
        title = DEMO_TITLES[index % DEMO_TITLES.size],
        price = DEMO_PRICES[index % DEMO_PRICES.size],
        neighborhood = "",
        postTime = "",
        image = DEMO_IMAGES[r.nextInt(DEMO_IMAGES.size)],
    )
}