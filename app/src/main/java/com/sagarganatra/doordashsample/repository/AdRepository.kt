package com.sagarganatra.doordashsample.repository

import com.sagarganatra.doordashsample.dataStore.SharedPrefDataStore
import com.sagarganatra.doordashsample.models.Ad
import io.reactivex.Maybe
import timber.log.Timber

/**
 * Interface that establishes contract with Repository
 *
 */
interface AdRepository {
    fun getAds(): Maybe<Ad>

    fun setDismiss(isDismissed: Boolean)
}

/**
 * Repository Implementation
 *
 */
class AdRepositoryImpl(
    private val prefs: SharedPrefDataStore
) : AdRepository {

    override fun getAds(): Maybe<Ad> {
        Timber.d("Pref is ${prefs.isAdDismissed}")
        // Check if dismissed, if not then return
        return if (prefs.isAdDismissed) Maybe.empty()
        else Maybe.just(Ad("Welcome to Doordash!!"))
    }

    override fun setDismiss(isDismissed: Boolean) {
        // Sets the SharedPref value to dismissed
        prefs.isAdDismissed = isDismissed
    }

}