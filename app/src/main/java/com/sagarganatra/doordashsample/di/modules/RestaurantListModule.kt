package com.sagarganatra.doordashsample.di.modules

import com.sagarganatra.doordashsample.api.RestaurantsApi
import com.sagarganatra.doordashsample.repository.RestaurantsRepository
import com.sagarganatra.doordashsample.repository.RestaurantsRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RestaurantListModule {

    @Provides
    fun provideRestaurantsApi(retrofit: Retrofit): RestaurantsApi {
        return retrofit.create(RestaurantsApi::class.java)
    }

    @Provides
    fun provideRestaurantRepository(restaurantsApi: RestaurantsApi): RestaurantsRepository {
        return RestaurantsRepositoryImpl(restaurantsApi)
    }
}