package com.fromu.fromu.utils.sns

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Logger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class GoogleLoginManager(private val mContext: Context, private val activityLauncher: ActivityResultLauncher<Intent>) {

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(Const.GOOGLE_CLIENT_ID)
        .requestEmail() // email addresses도 요청함
        .build()

    private val mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso)


    fun loginGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        signInIntent.run {
            activityLauncher.launch(this)
        }
    }

    fun checkLastSigned() {
        val gsa = GoogleSignIn.getLastSignedInAccount(mContext)

        if (gsa != null) {
            Logger.d("googleLoing", "로그인 되어 있음")
        } else {
            Logger.d("googleLoing", "로그인 안되어 있음")
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): String? {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val id = account?.id.toString()
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()
            val accessToken = account.idToken.toString()

            //TODO accessToken 서버와 연결
            Logger.d("googleLogin", id)
            Logger.d("googleLogin", email)
            Logger.d("googleLogin", familyName)
            Logger.d("googleLogin", givenName)
            Logger.d("googleLogin", displayName)
            Logger.d("googleLogin", accessToken)

            return accessToken
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.e("googleLogin", "GoogleSignInResult:failed code=" + e.statusCode)

            return null
        }

    }

    fun showUserInfo() {
        val gsa = GoogleSignIn.getLastSignedInAccount(mContext)

        if (gsa != null) {
            Logger.d("googleLogin", "Email: ${gsa.email}")
            Logger.d("googleLogin", "Name ${gsa.displayName}")
            Logger.d("googleLogin", "token ${gsa.idToken}")
        } else {
            Logger.d("googleLogin", "로그인 안되어 있음")
        }
    }

    fun logoutGoogle() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    Logger.d("googleLogin", "구글 로그아웃 성공")
                } else if (it.isCanceled) {
                    Logger.d("googleLogin", "구글 로그아웃 실패")
                } else {
                    Logger.d("googleLogin", "구글 로그아웃 나머지")
                }
            }
    }
}