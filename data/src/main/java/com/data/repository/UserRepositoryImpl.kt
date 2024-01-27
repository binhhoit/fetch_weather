package com.data.repository

import com.data.common.SharePreferenceManager
import java.util.UUID

class UserRepositoryImpl constructor(private var localData : SharePreferenceManager) : UserRepository {
    override suspend fun login(useName: String): Boolean {
        localData.firstOpenApp = true
        localData.userToken = UUID.randomUUID().toString()
        localData.userNameInfo = useName
        return true
    }

    override suspend fun getUserInfo(): String {
        return localData.userNameInfo
    }

    override suspend fun deleteAccount(): Boolean {
        localData.clearData()
        return true
    }

}
