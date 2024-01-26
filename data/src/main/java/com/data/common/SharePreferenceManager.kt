package com.data.common

import android.content.Context
import android.content.SharedPreferences
import com.data.model.LocationResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharePreferenceManager constructor(app: Context, private val gson: Gson) {

    companion object {
        private const val SHARED_PREF_NAME = "shared_pref"
        private const val USER_TOKEN = "user_token"
        private const val FIRST_OPEN_APP = "first_open_app"
        private const val USER_KEY = "user_key"
        private const val FAVORITE_LOCATION_KEY = "favorite_location_key"

        // For Singleton instantiation
        @Volatile
        private var instance: SharePreferenceManager? = null

        fun getInstance(context: Context, gson: Gson): SharePreferenceManager {
            return instance ?: synchronized(this) {
                instance ?: SharePreferenceManager(context, gson).also { instance = it }
            }
        }
    }

    private val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        app.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    var userToken: String
        get() = sharedPreferences.getString(USER_TOKEN, "")!!
        set(value) = sharedPreferences.put { putString(USER_TOKEN, value) }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    private inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
        val editor = this.edit()
        editor.body()
        editor.apply()
    }

    var firstOpenApp: Boolean
        get() = sharedPreferences.getBoolean(FIRST_OPEN_APP, true)
        set(value) = sharedPreferences.put { putBoolean(FIRST_OPEN_APP, value) }

    var userNameInfo: String
        get() = sharedPreferences.getString(USER_KEY, "") ?: ""
        set(value) = sharedPreferences.put { putString(USER_KEY, value) }

    var listLocationFavorite: List<LocationResponse>
        get() {
            val type = object : TypeToken<List<LocationResponse>>() {}.type
            return try {
                gson.fromJson(sharedPreferences.getString(
                    FAVORITE_LOCATION_KEY, ""), type) ?: listOf()
            } catch (e: Exception) {
                e.printStackTrace()
                listOf()
            }
        }
        set(value) = sharedPreferences.put {
            putString(FAVORITE_LOCATION_KEY,
                gson.toJson(value))
        }
}
