package com.data.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.Exception
import java.util.Locale

class SharePreferenceManager constructor(app: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "shared_pref"
        private const val USER_TOKEN = "user_token"
        private const val LOCALE_KEY = "locale_key"
        private const val FIRST_OPEN_APP = "first_open_app"
        private const val URI_FOLDER_KEY = "uri_folder_key"
        private const val USER_KEY = "user_key"

        // For Singleton instantiation
        @Volatile
        private var instance: SharePreferenceManager? = null

        fun getInstance(context: Context): SharePreferenceManager {
            return instance ?: synchronized(this) {
                instance ?: SharePreferenceManager(context).also { instance = it }
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
        sharedPreferences.edit().remove(USER_TOKEN).apply()
    }

    private inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
        val editor = this.edit()
        editor.body()
        editor.apply()
    }

    var localeCurrent: Locale
        get() = try {
            Gson().fromJson(sharedPreferences.getString(LOCALE_KEY,"en"), Locale::class.java)
        } catch (e: Exception) {
            Locale.ENGLISH
        }
        set(value) = sharedPreferences.put { putString(LOCALE_KEY, Gson().toJson(value)) }

    var firstOpenApp: Boolean
        get() = sharedPreferences.getBoolean(FIRST_OPEN_APP, true)
        set(value) = sharedPreferences.put { putBoolean(FIRST_OPEN_APP, value) }

    var userNameInfo: String
        get() = sharedPreferences.getString(USER_KEY, "") ?: ""
        set(value) = sharedPreferences.put { putString(USER_KEY, value) }
}
