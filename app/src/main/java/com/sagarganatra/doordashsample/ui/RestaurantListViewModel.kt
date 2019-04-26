package com.sagarganatra.doordashsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.models.Restaurant
import com.sagarganatra.doordashsample.repository.RestaurantsRepository
import com.sagarganatra.doordashsample.utils.LAT
import com.sagarganatra.doordashsample.utils.LONG
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import timber.log.Timber
import javax.inject.Inject

class RestaurantListViewModel @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository,
    private val schedulers: RxSchedulers
) : ViewModel() {

    private var actionSubscription = Disposables.disposed()
    private var subscriptions = CompositeDisposable()
    private val restaurantListLiveData = MutableLiveData<RestaurantListState>()

    fun getRestaurants(): LiveData<RestaurantListState> {
        return restaurantListLiveData
    }

    fun attach(actionStream: Observable<RestaurantListAction>) {
        actionSubscription.dispose()
        subscriptions = CompositeDisposable()
        actionSubscription = actionStream.subscribe(
            {handleRestaurantListActions(it)},
            {error -> Timber.e(error)}
        )
    }

    private fun loadRestaurants(lat: String, long: String) {
        restaurantListLiveData.postValue(RestaurantListState.Loading)
        subscriptions.clear()
        subscriptions.add(restaurantsRepository.getRestaurantsByLatLong(lat, long)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {response -> restaurantListLiveData.postValue(RestaurantListState.Restaurants(response))},
                {error ->
                    restaurantListLiveData.postValue(RestaurantListState.Error)
                    Timber.e(error.toString())
                }
            )
        )

    }

    private fun handleRestaurantListActions(action: RestaurantListAction) {
        return when(action) {
            RestaurantListAction.LoadRestaurants -> {
                loadRestaurants(LAT.toString(), LONG.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
        subscriptions.dispose()
        actionSubscription.dispose()
    }

    /*
    * Outgoing stream from ViewModel to Activity
    * Establishes contract between RestaurantListActivity and ViewModel
    * Handles RestaurantListActivity States
     */
    sealed class RestaurantListState {
        object Loading: RestaurantListState()
        object Error: RestaurantListState()
        data class Restaurants(val list: List<Restaurant>): RestaurantListState()
    }

    /*
   * Incoming stream from Activity to ViewModel
   * Establishes contract between RestaurantListActivity and ViewModel
   * Handles RestaurantListActivity Actions
   *
    */
    sealed class RestaurantListAction {
        object LoadRestaurants: RestaurantListAction()
    }

}