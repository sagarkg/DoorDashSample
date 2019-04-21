package com.sagarganatra.doordashsample

import android.app.Application
import com.sagarganatra.doordashsample.di.components.AppComponent
import com.sagarganatra.doordashsample.di.components.DaggerAppComponent
import com.sagarganatra.doordashsample.di.modules.AppModule
import javax.inject.Inject

class App: Application() {
    @Inject
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}