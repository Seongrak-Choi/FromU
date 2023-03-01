package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.SignupDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.SignupReq
import com.fromu.fromu.data.remote.network.response.SignupRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignupRepo @Inject constructor(private val signupDataSource: SignupDataSource) {
    suspend fun postSignup(signupReq: SignupReq): Flow<Resource<SignupRes>> {
        return signupDataSource.postSignup(signupReq)
    }
}