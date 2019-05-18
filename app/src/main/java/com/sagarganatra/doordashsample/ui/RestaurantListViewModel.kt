package com.sagarganatra.doordashsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.models.Ad
import com.sagarganatra.doordashsample.models.Restaurant
import com.sagarganatra.doordashsample.repository.AdRepository
import com.sagarganatra.doordashsample.repository.RestaurantsRepository
import com.sagarganatra.doordashsample.utils.LAT
import com.sagarganatra.doordashsample.utils.LONG
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class RestaurantListViewModel @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository,
    private val adRepository: AdRepository,
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

    private fun loadRestaurantsAndAds(lat: String, long: String) {


        restaurantListLiveData.postValue(RestaurantListState.Loading)
        subscriptions.clear()
        subscriptions.add(

            restaurantsRepository.getRestaurantsByLatLong(lat, long)
                .subscribeOn(schedulers.io())
                .zipWith(adRepository.getAds(),
                    BiFunction{t1: List<Restaurant>, t2: Ad  -> RestaurantListState.ViewState(t1, t2)})
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe(
                    {response -> restaurantListLiveData.postValue(response)},
                    {error ->
                        restaurantListLiveData.postValue(RestaurantListState.Error)
                        Timber.e(error.toString())
                    }
                )
        )
    }

//    private fun loadAds() {
//        // subscriptions.clear()
//        subscriptions.add(
//            adRepository.getAds()
//                .subscribeOn(schedulers.io())
//                .observeOn(schedulers.mainThread())
//                .subscribe(
//                    {response -> restaurantListLiveData.postValue(RestaurantListState.Ads(response))},
//                    {error -> Timber.e(error.toString())}
//                )
//        )
//    }


    private fun handleRestaurantListActions(action: RestaurantListAction) {
        return when(action) {
            RestaurantListAction.LoadRestaurantsAndAds -> {
                loadRestaurantsAndAds(LAT.toString(), LONG.toString())
            }
            RestaurantListAction.DismissAds -> {
                adRepository.setDismiss(true)
                restaurantListLiveData.postValue(RestaurantListState.DismissAd)
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
        object DismissAd: RestaurantListState()
        data class ViewState(
            val list: List<Restaurant>,
            val ad: Ad
        ): RestaurantListState()
    }

    /*
   * Incoming stream from Activity to ViewModel
   * Establishes contract between RestaurantListActivity and ViewModel
   * Handles RestaurantListActivity Actions
   *
    */
    sealed class RestaurantListAction {
        object LoadRestaurantsAndAds: RestaurantListAction()
        object DismissAds: RestaurantListAction()
    }

}