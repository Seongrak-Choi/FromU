package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.LoginService
import com.fromu.fromu.data.remote.network.response.LoginResponse
import com.fromu.fromu.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LoginDataSource @Inject constructor(private val loginService: LoginService) {
    suspend fun loginWithKakao(accessToken: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = loginService.kakaoLogin(accessToken)
            Logger.e("rak", "LoginDataSource ${response.body().toString()}")
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Failed(response.message()))
            }
        } catch (e: java.lang.Exception) {
            emit(Resource.Failed(e.message ?: "An unkown error occurred"))
        }
    }

    suspend fun loginWithGoogle(accessToken: String): MutableStateFlow<Resource<LoginResponse>> {
        //TODO 추후 구현
        return MutableStateFlow(Resource.Failed(""))
    }

    suspend fun loginWithJwt(): MutableStateFlow<Resource<LoginResponse>> {
        //TODO 추후 구현
        return MutableStateFlow(Resource.Failed(""))
    }
}