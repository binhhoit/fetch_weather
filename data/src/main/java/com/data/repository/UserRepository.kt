package com.data.repository

interface UserRepository {
    suspend fun login(useName: String): Boolean
    suspend fun getUserInfo():String
    suspend fun deleteAccount():Boolean
}
