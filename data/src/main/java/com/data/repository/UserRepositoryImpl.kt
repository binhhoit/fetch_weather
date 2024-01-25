package com.data.repository

import com.data.exception.UnAuthException
import com.data.network.remote.ServiceAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl constructor(private var api: ServiceAPI) : UserRepository {
    override fun login(email: String, password: String): Flow<String> {
        return flow {
            delay(2500)
            if(email == "test@gmail.com" && password == "Password") {
                emit("Success")
            } else {
                throw UnAuthException()
            }
        }
    }

    override fun register(email: String, password: String): Flow<String> {
        return flow {
            delay(2500)
            if(email == "test@gmail.com") {
                throw UnAuthException()
            } else {
                emit("Success")
            }
        }
    }
}
