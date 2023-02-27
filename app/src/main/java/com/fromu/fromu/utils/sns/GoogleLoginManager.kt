package com.fromu.fromu.utils.sns

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.fromu.fromu.utils.Const
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import timber.log.Timber


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
            Timber.tag("ator").d("로그인 되어 있음")
        } else {
            Timber.tag("ator").d("로그인 안되어 있음")
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val id = account?.id.toString()
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()
            val accessToken = account.idToken.toString()

            //TODO accessToken 서버와 연결
            Timber.tag("rak").e(accessToken)


            Timber.tag("ator").d(id)
            Timber.tag("ator").d(email)
            Timber.tag("ator").d(familyName)
            Timber.tag("ator").d(givenName)
            Timber.tag("ator").d(displayName)
            Timber.tag("ator").d(accessToken)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.tag("ator").e("GoogleSignInResult:failed code=" + e.statusCode)
        }
    }

    fun showUserInfo() {
        val gsa = GoogleSignIn.getLastSignedInAccount(mContext)

        if (gsa != null) {
            Timber.tag("ator").d("Email: %s", gsa.email.toString())
            Timber.tag("ator").d("Name %s", gsa.displayName.toString())
            Timber.tag("ator").d("token %s", gsa.idToken.toString())
        } else {
            Timber.tag("ator").e("로그인 안되어 있음")
        }
    }

    fun logoutGoogle() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    Timber.tag("ator").d("구글 로그아웃 성공")
                } else if (it.isCanceled) {
                    Timber.tag("ator").d("구글 로그아웃 실패")
                } else {
                    Timber.tag("ator").d("구글 로그아웃 나머지")
                }
            }
    }
}