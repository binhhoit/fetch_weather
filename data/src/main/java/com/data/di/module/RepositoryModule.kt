package com.data.di.module

import com.data.repository.LocationRepository
import com.data.repository.LocationRepositoryImpl
import com.data.repository.UserRepository
import com.data.repository.UserRepositoryImpl
import com.data.repository.WeatherRepository
import com.data.repository.WeatherRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
}
