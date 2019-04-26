package com.sagarganatra.doordashsample.di.components

import com.sagarganatra.doordashsample.di.modules.PerActivity
import com.sagarganatra.doordashsample.di.modules.RestaurantListModule
import com.sagarganatra.doordashsample.di.modules.RetrofitModule
import com.sagarganatra.doordashsample.ui.RestaurantListActivity
import dagger.Component

@PerActivity
@Component(
    dependencies = [AppComponent::class],
    modules = [RestaurantListModule::class, RetrofitModule::class]
)
interface RestaurantListComponent{
    fun inject(restaurantListActivity: RestaurantListActivity)
}