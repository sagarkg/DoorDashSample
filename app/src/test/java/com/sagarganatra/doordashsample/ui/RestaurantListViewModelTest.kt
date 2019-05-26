package com.sagarganatra.doordashsample.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.sagarganatra.doordashsample.core.TestSchedulers
import com.sagarganatra.doordashsample.utils.getRestaurantListSampleResponse
import com.sagarganatra.doordashsample.repository.RestaurantsRepository
import com.sagarganatra.doordashsample.ui.restaurantlist.RestaurantListViewModel
import com.sagarganatra.doordashsample.ui.restaurantlist.RestaurantListViewModel.RestaurantListAction
import com.sagarganatra.doordashsample.ui.restaurantlist.RestaurantListViewModel.RestaurantListState
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`

class RestaurantListViewModelTest {

    @get:Rule
    var testScheduler: TestRule = InstantTaskExecutorRule()

    // ViewModel to be tested
    private lateinit var vm: RestaurantListViewModel

    // Repository
    private lateinit var repo: RestaurantsRepository

    // Test ActionStream which would emit events
    private val actionStream: PublishRelay<RestaurantListAction> = PublishRelay.create()

    // Livedata Observer
    private lateinit var testObserver: Observer<RestaurantListState>

    @Before
    fun setUp() {

        repo = mock()
        testObserver = mock()

        vm = RestaurantListViewModel(repo, TestSchedulers())
        vm.getRestaurants().observeForever(testObserver)

        vm.attach(actionStream)
    }

    @Test
    fun loadRestaurantsAction_shouldCallRestaurantRepository() {
        actionStream.accept(RestaurantListAction.LoadRestaurants)
        verify(repo, times(1)).getRestaurantsByLatLong(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString()
        )
    }

    @Test
    fun whenApiIsCalled_shouldShowLoadingState() {
        actionStream.accept(RestaurantListAction.LoadRestaurants)
        verify(testObserver).onChanged(RestaurantListState.Loading)
    }

    @Test
    fun whenApiErrorsOut_shouldShowErrorState() {
        `when`(repo.getRestaurantsByLatLong(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(Single.error(Exception()))
        actionStream.accept(RestaurantListAction.LoadRestaurants)
        verify(testObserver).onChanged(RestaurantListState.Error)
    }

    @Test
    fun whenApiIsSuccessful_shouldShowValidResponse() {

        val captor = argumentCaptor<RestaurantListState.Restaurants>()

        `when`(repo.getRestaurantsByLatLong(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(Single.just(getRestaurantListSampleResponse()))
        actionStream.accept(RestaurantListAction.LoadRestaurants)
        verify(testObserver, times(2)).onChanged(captor.capture())

        val response = captor.lastValue.list
        assertEquals(response.count(), 2)
        assertEquals(response[0].id, 1)
        assertEquals(response[1].id, 2)
        assertEquals(response[0].business.name, "restaurant1")
        assertEquals(response[1].business.name, "restaurant2")
        assertFalse(response[1].status == "Some status")
    }
}