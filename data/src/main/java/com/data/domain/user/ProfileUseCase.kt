package com.data.domain.user

import com.data.repository.UserRepository
import kotlinx.coroutines.flow.flow

class ProfileUseCase constructor(private val userRepository: UserRepository) {
    fun logOutProfile() = flow {
        emit(userRepository.deleteAccount())
    }

    fun getProfileInfo() = flow {
        emit(userRepository.getUserInfo())
    }
}