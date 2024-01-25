package com.data.di.module

import com.data.domain.user.LoginUserCase
import org.koin.dsl.module

val useCaseModule = module  {
    factory { LoginUserCase(get()) }
}