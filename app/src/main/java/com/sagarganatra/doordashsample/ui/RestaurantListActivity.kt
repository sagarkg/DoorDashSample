package com.sagarganatra.doordashsample.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxrelay2.PublishRelay
import com.sagarganatra.doordashsample.App
import com.sagarganatra.doordashsample.R
import com.sagarganatra.doordashsample.di.components.DaggerRestaurantListComponent
import com.sagarganatra.doordashsample.di.components.RestaurantListComponent
import com.sagarganatra.doordashsample.ui.RestaurantListViewModel.RestaurantListAction
import com.sagarganatra.doordashsample.ui.RestaurantListViewModel.RestaurantListState
import com.sagarganatra.doordashsample.utils.gone
import com.sagarganatra.doordashsample.utils.visible
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import kotlinx.android.synthetic.main.row_ads.*
import timber.log.Timber
import javax.inject.Inject

class RestaurantListActivity : AppCompatActivity() {

    @Inject
    lateinit var restaurantListViewModelFactory: RestaurantListViewModelFactory
    private lateinit var restaurantListComponent: RestaurantListComponent
    private lateinit var restaurantListViewModel: RestaurantListViewModel
    private val actionStream: PublishRelay<RestaurantListAction> = PublishRelay.create()
    private lateinit var adapter: RestaurantListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = resources.getString(R.string.restaurant_list_title)
        }

        restaurantListComponent = DaggerRestaurantListComponent.builder()
            .appComponent((application as App).appComponent)
            .build()
        restaurantListComponent.inject(this)

        restaurantListViewModel = ViewModelProviders.of(this, restaurantListViewModelFactory)
            .get(RestaurantListViewModel::class.java)

        restaurantListViewModel.getRestaurants().observe(this, Observer {
            if(it != null) this.handleRestaurantListState(it)
        })

        // Send ActionStream to ViewModel
        restaurantListViewModel.attach(actionStream)

        linearLayoutManager = LinearLayoutManager(this)
        restaurantListRecyclerView.layoutManager = linearLayoutManager
        val divider = DividerItemDecoration(restaurantListRecyclerView.context, linearLayoutManager.orientation)
        restaurantListRecyclerView.addItemDecoration(divider)

        // Send action to actionStream to get RestaurantList and Ads
        actionStream.accept(RestaurantListAction.LoadRestaurantsAndAds)

        // Send action to actionStream to get Ads
        // actionStream.accept(RestaurantListAction.LoadAds)


        tryAgainTextView.setOnClickListener{
            actionStream.accept(RestaurantListAction.LoadRestaurantsAndAds)
        }

//        dismissTextView.setOnClickListener{
//            actionStream.accept(RestaurantListAction.DismissAds)
//        }
    }

    private fun handleRestaurantListState(state: RestaurantListState) {
        when(state) {
            RestaurantListState.Loading -> {
                progressBar.visible()
                restaurantListRecyclerView.gone()
                errorTextView.gone()
                tryAgainTextView.gone()
            }
            RestaurantListState.Error -> {
                progressBar.gone()
                restaurantListRecyclerView.gone()
                errorTextView.visible()
                errorTextView.text = resources.getString(R.string.error)
                tryAgainTextView.visible()
            }
            is RestaurantListState.ViewState -> {
                progressBar.gone()
                errorTextView.gone()
                restaurantListRecyclerView.visible()
                adapter = RestaurantListAdapter(this, state.list, state.ad)
                restaurantListRecyclerView.adapter = adapter
            }
            RestaurantListState.DismissAd -> {
                //adsLayout.gone()
                actionStream.accept(RestaurantListAction.DismissAds)
            }

        }
    }
}
