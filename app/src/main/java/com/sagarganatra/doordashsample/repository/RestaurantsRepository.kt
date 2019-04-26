package com.sagarganatra.doordashsample.repository

import com.sagarganatra.doordashsample.api.RestaurantsApi
import com.sagarganatra.doordashsample.models.Restaurant
import io.reactivex.Single

/**
 * Interface that establishes contract with Repository
 *
 */
interface RestaurantsRepository {
    fun getRestaurantsByLatLong(lat: String, long: String): Single<List<Restaurant>>
}


/**
 * Repository Implementation
 *
 */
class RestaurantsRepositoryImpl(
    private val api: RestaurantsApi
) : RestaurantsRepository {

    override fun getRestaurantsByLatLong(lat: String, long: String): Single<List<Restaurant>> {
        return api.getRestaurantByLatLong(lat, long)
    }

}