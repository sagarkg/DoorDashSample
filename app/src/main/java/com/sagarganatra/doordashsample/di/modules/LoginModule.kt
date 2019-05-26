package com.sagarganatra.doordashsample.di.modules

import com.sagarganatra.doordashsample.api.LoginApi
import com.sagarganatra.doordashsample.dataStore.SharedPrefDataStore
import com.sagarganatra.doordashsample.repository.LoginRepository
import com.sagarganatra.doordashsample.repository.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class LoginModule {

    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    fun provideLoginRepository(
        loginApi: LoginApi,
        pref: SharedPrefDataStore
    ): LoginRepository {
        return LoginRepositoryImpl(loginApi, pref)
    }

}