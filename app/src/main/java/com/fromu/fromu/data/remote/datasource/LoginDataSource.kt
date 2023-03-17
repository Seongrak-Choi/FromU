package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.BuildConfig
import com.fromu.fromu.data.dto.GoogleSignInAccessTokenDataClass
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.LoginService
import com.fromu.fromu.data.remote.network.request.PatchFcmTokenReq
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.PatchFcmTokenRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Logger
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        emit(Resource.Failed("loginWithKakao: An unkown error occurred"))
    }

    suspend fun loginWithGoogle(accessToken: String): Flow<Resource<SNSLoginRes>> = flow {
        emit(Resource.Loading)

        val response = loginService.GoogleLogin(accessToken)

        if (response.isSuccessful) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Failed(response.message()))
        }
    }.catch {
        emit(Resource.Failed("loginWithGoogle: An unkown error occurred"))
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


    suspend fun getGoogleAccessToken(authCode: String, idToken: String): Flow<Resource<GoogleSignInAccessTokenDataClass>> = callbackFlow {
        trySend(Resource.Loading)

        loginService.getAccessTokenGoogle(
            grant_type = "authorization_code", client_id = BuildConfig.GOOGLE_CLIENT_ID, client_secret = BuildConfig.GOOGLE_CLIENT_SECRET,
            redirect_uri = "", authCode = authCode, id_token = idToken
        ).enqueue(object : Callback<GoogleSignInAccessTokenDataClass> {
            override fun onResponse(call: Call<GoogleSignInAccessTokenDataClass>, response: Response<GoogleSignInAccessTokenDataClass>) {
                trySend(Resource.Success(response.body()!!))
            }

            override fun onFailure(call: Call<GoogleSignInAccessTokenDataClass>, t: Throwable) {
                trySend(Resource.Failed(t.message ?: "통신 실패"))
            }
        })

        awaitClose()
    }.catch {
        emit(Resource.Failed("getGoogleAccessToken: An unkown error occurred"))
    }

    /**
     * fcm토큰 서버에 저장 api
     *
     * @param patchFcmTokenReq
     */
    fun patchDeviceToken(jwt: String, patchFcmTokenReq: PatchFcmTokenReq) {
        loginService.patchUsersDeviceToken(jwt, patchFcmTokenReq).enqueue(object : Callback<PatchFcmTokenRes> {
            override fun onResponse(call: Call<PatchFcmTokenRes>, response: Response<PatchFcmTokenRes>) {
                when (response.body()?.code) {
                    Const.SUCCESS_CODE -> {
                        Logger.e("patchFcmToken", "fcm 등록 성공")
                    }
                    else -> {
                        Logger.e("patchFcmToken", "fcm 등록 실패")
                    }
                }
            }

            override fun onFailure(call: Call<PatchFcmTokenRes>, t: Throwable) {
                Logger.e("patchFcmToken", t.message ?: "통신 오류")
            }
        })
    }
}