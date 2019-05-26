package com.sagarganatra.doordashsample.repository

import com.sagarganatra.doordashsample.api.LoginApi
import com.sagarganatra.doordashsample.dataStore.SharedPrefDataStore
import com.sagarganatra.doordashsample.models.LoginRequest
import com.sagarganatra.doordashsample.models.TokenResponse
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

interface LoginRepository {
    fun getAuthToken(loginRequest: LoginRequest): Single<TokenResponse>
    fun checkIfTokenExists(): Boolean
}


class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val pref: SharedPrefDataStore
): LoginRepository {

    override fun getAuthToken(loginRequest: LoginRequest): Single<TokenResponse> {
        // Save to SharedPref
        return api.getAuthToken(loginRequest)
            // TODO - Remove test code
//            .onErrorResumeNext {
//                Single.just(TokenResponse("443sknksnf334skdfs"))
//            }
            .map {
                pref.token = it.token
                Timber.d(it.token)
                it
            }
    }

    override fun checkIfTokenExists(): Boolean {
        return (pref.token).isNotEmpty()
    }
}