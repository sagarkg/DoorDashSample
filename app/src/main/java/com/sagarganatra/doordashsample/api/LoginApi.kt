package com.sagarganatra.doordashsample.api

import com.sagarganatra.doordashsample.models.LoginRequest
import com.sagarganatra.doordashsample.models.TokenResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("auth/token/")
    fun getAuthToken(@Body body: LoginRequest): Single<TokenResponse>
}