package com.example.panedemo.panecomparison.common.product

import com.example.panedemo.R

data class ProductUiState(
    val localId: Long = 0,
    val id: String,
    val title: String,
    val priceWithCurrency: String?,
    val address: String?,
    val location: ProductLocation?,
    val postedDate: String,
    val visits: String,
    val features: List<String> = emptyList(),
    val pictures: List<Int>,
    val description: String,
)
{
    companion object {
        fun createInitialModel(): ProductUiState = ProductUiState(
            id = "1118635128",
            title = "Spacious top-floor apartment",
            priceWithCurrency = "€859,976",
            address = "Weserstraße 15, 12047 Berlin",
            location = ProductLocation(
                latitude = 52.4874554,
                longitude = 13.4285519
            ),
            postedDate = "10.08.2021",
            visits = "2869",
            features = listOf(
                "Balcony",
                "Garage / Parking space",
                "Shower",
                "Elevator",
                "Basement",
                "Bathtub",
                "Fitted kitchen",
                "Barrier-free",
                "Garden",
                "Fireplace",
                "Sauna",
                "Terrace",
            ),
            pictures = listOf(R.drawable.demo_apartment_1, R.drawable.demo_apartment_2, R.drawable.demo_apartment_3, R.drawable.demo_apartment_4, R.drawable.demo_apartment_5),
            description = "The building\n\nThe timelessly understated building, with its high ceilings, inviting plank floors and stylish stucco elements, is a perfect example of the enduring beauty of period architecture. Built around 1900, the property follows the classic layout of a front house, a side wing and an adjoining transverse building. All apartments also feature a balcony, from which you can relax and take in the Neukölln neighborhood atmosphere. The building was recently modernized, so the entrance area, stairwells, façade and inner courtyard now shine in fresh splendor.\n\nThe apartment\n\nTop-floor apartments are highly sought after and rare — especially when they shine in freshly renovated splendor and boast such a large roof terrace as this 3-room top-floor apartment of around 107 square meters. Add to that one of Neukölln's most popular neighborhoods right on the doorstep. With the sun as a constant guest, you'll experience plenty of joy here. In addition to a large living room with access to the beautiful roof terrace, the apartment has two more rooms, which you enter from the elegant hallway. The renovation includes fresh paint on walls and ceilings, new doors in every room, new parquet flooring, new tiles in the kitchen, and a completely new bathroom. Don't miss this exceptional top-floor apartment.\n\nThe location\n\nNeukölln is one of Berlin's most colorful and exciting districts. The northern part in particular, with its emerging cultural scene, has been reinvigorated in recent years and has benefited enormously from neighboring Kreuzberg. So-called Kreuzkölln, home to the Reuterkiez, is an attractive residential area with countless beautiful Wilhelminian-era buildings. Typical of this area are the many trendy bars, galleries, cafés and shops. Young, well-educated people, artists, creatives and families are especially drawn here. Plenty of nearby options for recreation are also within easy reach: the Maybachufer, Volkspark Hasenheide and Tempelhofer Feld. Altogether, the image, urban vibe and central location make Kreuzkölln a magnet for a young, dynamic crowd."
        )
    }
}
data class ProductLocation(
    val latitude: Double,
    val longitude: Double
)
