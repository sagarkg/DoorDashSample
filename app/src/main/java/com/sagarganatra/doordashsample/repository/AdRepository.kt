package com.sagarganatra.doordashsample.repository

import com.sagarganatra.doordashsample.dataStore.SharedPrefDataStore
import com.sagarganatra.doordashsample.models.Ad
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber

/**
 * Interface that establishes contract with Repository
 *
 */
interface AdRepository {
    fun getAds(): Single<Ad>

    fun setDismiss(isDismissed: Boolean)
}

/**
 * Repository Implementation
 *
 */
class AdRepositoryImpl(
    private val prefs: SharedPrefDataStore
) : AdRepository {

    override fun getAds(): Single<Ad> {
        Timber.d("Pref is ${prefs.isAdDismissed}")
        // Check if dismissed, if not then return
        return if (prefs.isAdDismissed) Single.just(Ad("", true))
        else Single.just(Ad("Welcome to Doordash!!", false))
    }

    override fun setDismiss(isDismissed: Boolean) {
        // Sets the SharedPref value to dismissed
        prefs.isAdDismissed = isDismissed
    }

}