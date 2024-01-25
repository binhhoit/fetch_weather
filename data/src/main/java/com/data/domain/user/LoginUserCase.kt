package com.data.domain.user

import com.data.domain.UseCase
import com.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUserCase(private val userRepository: UserRepository) :
    UseCase<String, Flow<Boolean>>() {
    override fun execute(param: String?) = flow {
        if (param == null)
            throw Exception("Invalid user name")
        emit(userRepository.login(param))
    }
}
