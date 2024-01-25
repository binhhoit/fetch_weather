package com.fetch.weather.di.module

import com.fetch.weather.ui.feature.dashboard.search.SearchViewModel
import com.fetch.weather.ui.feature.dashboard.search.SearchViewModelImpl
import com.fetch.weather.ui.feature.login.LoginViewModel
import com.fetch.weather.ui.feature.login.LoginViewModelImpl
import com.fetch.weather.ui.feature.welcome.WelcomeViewModel
import com.fetch.weather.ui.feature.welcome.WelcomeViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<WelcomeViewModel> { WelcomeViewModelImpl(get()) }
    viewModel<LoginViewModel> { LoginViewModelImpl(get()) }
    viewModel<SearchViewModel> { SearchViewModelImpl(get()) }
}