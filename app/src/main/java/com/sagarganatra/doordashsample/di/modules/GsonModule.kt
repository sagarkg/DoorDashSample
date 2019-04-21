package com.sagarganatra.doordashsample.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory

@Module
class GsonModule {

    @Provides
    fun provideGson() : Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}