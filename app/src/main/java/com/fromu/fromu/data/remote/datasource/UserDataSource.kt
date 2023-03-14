package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.UserService
import com.fromu.fromu.data.remote.network.response.LogoutRes
import com.fromu.fromu.data.remote.network.response.WithdrawalRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSource @Inject constructor(private val userService: UserService) {
    fun logout(): Flow<Resource<LogoutRes>> = flow {
        emit(Resource.Loading)

        val res = userService.logout()

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("logout: An unkown error occurred"))
    }

    fun withdrawal(): Flow<Resource<WithdrawalRes>> = flow {
        emit(Resource.Loading)

        val res = userService.withdrawal()

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("withdrawal: An unkown error occurred"))
    }
}