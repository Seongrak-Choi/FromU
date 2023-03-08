package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.LoginService
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LoginDataSource @Inject constructor(private val loginService: LoginService) {
    suspend fun loginWithKakao(accessToken: String): Flow<Resource<SNSLoginRes>> = flow {
        emit(Resource.Loading)

        val response = loginService.kakaoLogin(accessToken)
        if (response.isSuccessful) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Failed(response.message()))
        }
    }.catch {
        emit(Resource.Failed("An unkown error occurred"))
    }

    suspend fun loginWithGoogle(accessToken: String): MutableStateFlow<Resource<SNSLoginRes>> {
        //TODO 추후 구현
        return MutableStateFlow(Resource.Failed(""))
    }

    suspend fun loginWithJwt(jwt: String): Flow<Resource<JWTLoginRes>> = flow {
        emit(Resource.Loading)

        val res = loginService.jwtLogin(jwt)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("loginWithGoogle: An unkown error occurred"))
    }
}