package com.sagarganatra.doordashsample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.repository.RestaurantsRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RestaurantListViewModelFactory @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository,
    private val schedulers: RxSchedulers
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RestaurantListViewModel::class.java)) {
            return RestaurantListViewModel(restaurantsRepository, schedulers) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}