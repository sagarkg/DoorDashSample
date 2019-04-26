package com.sagarganatra.doordashsample.api

import com.sagarganatra.doordashsample.models.Restaurant
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantsApi {

    @GET("restaurant")
    fun getRestaurantByLatLong(@Query("lat") lat: String,
                               @Query("lng") long: String
    ): Single<List<Restaurant>>
}