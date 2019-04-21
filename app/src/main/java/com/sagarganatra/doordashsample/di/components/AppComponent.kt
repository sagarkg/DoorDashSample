package com.sagarganatra.doordashsample.di.components

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import com.sagarganatra.doordashsample.App
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: Activity)


    //Exposed to sub graphs
    val app: App
    val context: Context
    val resources: Resources
    val schedulers: RxSchedulers
}