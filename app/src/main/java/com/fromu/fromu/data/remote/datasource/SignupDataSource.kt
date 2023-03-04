package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.SignupService
import com.fromu.fromu.data.remote.network.request.SignupReq
import com.fromu.fromu.data.remote.network.response.SignupRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignupDataSource @Inject constructor(private val signupService: SignupService) {

    suspend fun postSignup(signupReq: SignupReq): Flow<Resource<SignupRes>> = flow {
        emit(Resource.Loading)

        val response = signupService.postSignup(signupReq)
        if (response.isSuccessful) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Failed(response.message()))
        }

    }.catch {
        emit(Resource.Failed("An unkown error occurred"))
    }
}