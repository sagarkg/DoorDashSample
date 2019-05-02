package com.sagarganatra.doordashsample.utils

import com.sagarganatra.doordashsample.models.Business
import com.sagarganatra.doordashsample.models.Restaurant

fun getRestaurantListSampleResponse(): List<Restaurant> {

    val business1 = Business(1, "restaurant1")
    val restaurant1 = Restaurant(
        1, business1,
        "description1",
        "url1",
        "open",
        "$3"
    )

    val business2 = Business(2, "restaurant2")
    val restaurant2 = Restaurant(
        2, business2,
        "description2",
        "url2",
        "closed",
        "$0"
    )

    return listOf(restaurant1, restaurant2)
}