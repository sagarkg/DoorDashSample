package com.sagarganatra.doordashsample.dataStore

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface SharedPrefDataStore {

    var isAdDismissed: Boolean
    var token: String
}


class SharedPrefDataStoreImpl @Inject constructor(
    context: Context
) : SharedPrefDataStore {


    private val pref = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "authToken"
        const val IS_AD_DISMISSED = "isAdDismissed"
        const val PREFS_NAME = "doorDashPrefs"
    }

    override var isAdDismissed by BooleanPreference(pref, IS_AD_DISMISSED, false)
    override var token by StringPreference(pref, AUTH_TOKEN, "")
}


class BooleanPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: Boolean
): ReadWriteProperty<Any, Boolean> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.edit().putBoolean(name, value).apply()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

}

class StringPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.getString(name, defaultValue)!!
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        preferences.edit().putString(name, value).apply()
    }
}
