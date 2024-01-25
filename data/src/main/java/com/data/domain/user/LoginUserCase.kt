package com.data.domain.user

import com.data.domain.UseCase
import com.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class LoginUserCase(private val userRepository: UserRepository) :
    UseCase<LoginUserCase.Param, Flow<String>>() {
    override fun execute(param: Param?) : Flow<String> {
        return userRepository.login(param!!.name, param.password)
    }

    class Param(val name: String, val password: String)
}
