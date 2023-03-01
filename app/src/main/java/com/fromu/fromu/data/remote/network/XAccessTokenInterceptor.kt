package com.fromu.fromu.data.remote.network

import android.content.SharedPreferences
import com.fromu.fromu.utils.PrefManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *  헤더에 JWT 자동으로 전송하기 위한 Interceptor 클래스
 */
class XAccessTokenInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwtToken: String? = sharedPreferences.getString(PrefManager.X_ACCESS_TOKEN, null)
        if (jwtToken != null) {
            builder.addHeader(PrefManager.X_ACCESS_TOKEN, jwtToken)
        }
        return chain.proceed(builder.build())
    }
}