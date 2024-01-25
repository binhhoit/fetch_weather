package com.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(email: String, password: String): Flow<String>

    fun register(email: String, password: String): Flow<String>
}
