package com.fromu.fromu.data.remote.network.api

import com.fromu.fromu.data.dto.GoogleSignInAccessTokenDataClass
import com.fromu.fromu.data.remote.network.request.PatchFcmTokenReq
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.data.remote.network.response.PatchFcmTokenRes
import com.fromu.fromu.data.remote.network.response.SNSLoginRes
import com.fromu.fromu.utils.PrefManager
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface LoginService {
    @POST("users/kakao")
    suspend fun kakaoLogin(@Header(PrefManager.X_ACCESS_TOKEN) header: String): Response<SNSLoginRes>

    @POST("users/google")
    suspend fun GoogleLogin(@Header(PrefManager.X_ACCESS_TOKEN) header: String): Response<SNSLoginRes>


    /**
     * jwt로 로그인하고 실패 시 refreshToken으로 로그인
     *
     * @param header
     * @return
     */
    @POST("users/refreshToken")
    suspend fun jwtLogin(@Header(PrefManager.X_ACCESS_TOKEN) header: String): Response<JWTLoginRes>


    /**
     * 구글 idToken으로 accessToken 받아오는 api
     *
     * @param url
     * @param grant_type
     * @param client_id
     * @param client_secret
     * @param redirect_uri
     * @param authCode
     * @param id_token
     * @return
     */
    @FormUrlEncoded
    @POST
    fun getAccessTokenGoogle(
        @Url url: String = "https://www.googleapis.com/oauth2/v4/token",
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code") authCode: String,
        @Field("id_token") id_token: String
    ): Call<GoogleSignInAccessTokenDataClass>


    @PATCH("users/deviceToken")
    fun patchUsersDeviceToken(@Header(PrefManager.X_ACCESS_TOKEN) header: String, @Body patchFcmTokenReq: PatchFcmTokenReq): Call<PatchFcmTokenRes>
}