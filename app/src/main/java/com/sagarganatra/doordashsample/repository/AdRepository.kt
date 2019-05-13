package com.sagarganatra.doordashsample.repository

import com.sagarganatra.doordashsample.models.Ads
import io.reactivex.Single

/**
 * Interface that establishes contract with Repository
 *
 */
interface AdRepository {
    fun getAds(): Single<Ads>

    fun setDismiss()
}

/**
 * Repository Implementation
 *
 */
class AdRepositoryImpl() : AdRepository {

    override fun getAds(): Single<Ads> {
        return Single.just(Ads("Welcome to Doordash!!"))
    }

    override fun setDismiss() {
        // Sets the SharedPref value
    }

}