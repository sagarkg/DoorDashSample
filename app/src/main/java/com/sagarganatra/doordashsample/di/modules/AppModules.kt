package com.sagarganatra.doordashsample.di.modules

import android.content.Context
import android.content.res.Resources
import com.sagarganatra.doordashsample.App
import com.sagarganatra.doordashsample.core.DefaultSchedulers
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.dataStore.SharedPrefDataStore
import com.sagarganatra.doordashsample.dataStore.SharedPrefDataStoreImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApp(): App = app

    @Provides
    @Singleton
    fun provideContext(): Context = this.app

    @Provides
    @Singleton
    fun provideResources(): Resources = app.resources

    @Provides
    @Singleton
    fun provideSchedulers(): RxSchedulers = DefaultSchedulers()

    @Provides
    @Singleton
    fun provideSharedPrefsDataStore(context: Context): SharedPrefDataStore {
        return SharedPrefDataStoreImpl(context)
    }
}