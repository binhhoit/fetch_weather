package com.fetch.weather.di.module

import com.data.common.SharePreferenceManager
import com.fetch.weather.utils.Constants.API_DATE_TIME_FORMAT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val appModule = module() {
    single { SharePreferenceManager.getInstance(get()) }

    single<Gson> {
        GsonBuilder()
            // .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .setDateFormat(API_DATE_TIME_FORMAT)
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
    }
}
