package com.data.di.module

import com.data.domain.location.GeocodingFavoriteUseCase
import com.data.domain.location.GeocodingUseCase
import com.data.domain.user.LoginUseCase
import com.data.domain.user.ProfileUseCase
import com.data.domain.weather.WeatherUseCase
import org.koin.dsl.module

val useCaseModule = module  {
    factory { LoginUseCase(get()) }
    factory { GeocodingUseCase(get()) }
    factory { WeatherUseCase(get()) }
    factory { GeocodingFavoriteUseCase(get()) }
    factory { ProfileUseCase(get()) }
}