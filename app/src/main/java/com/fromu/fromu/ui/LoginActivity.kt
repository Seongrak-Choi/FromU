package com.fromu.fromu.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.fromu.fromu.databinding.ActivityLoginBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.GoogleLoginManager
import com.fromu.fromu.utils.KakaoLoginManager
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var googleLoginManagerInstance: GoogleLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        //google 로그인 callback 용
        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                googleLoginManagerInstance.handleSignInResult(task)
            } else {
                Timber.tag("ator").e("구글 로그인 실패")
            }
        }

        googleLoginManagerInstance = GoogleLoginManager(this, activityLauncher)
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
        binding.apply {
            viewmodels = loginViewModel
            view = this@LoginActivity
        }
    }

    /**
     * 카카오 로그인
     */
    fun loginKakao() {
        KakaoLoginManager(this).loginKakao(object : KakaoLoginManager.OnKakaoLoginListener {
            override fun onSuccess(accessToken: String) {
                Timber.tag("ator").e(accessToken)
                //TODO accessToken 서버와 연결
            }

            override fun onFailure(errorMsg: String) {
                Timber.tag("ator").e(errorMsg)
            }
        })
    }

    /**
     * 구글 로그인
     */
    fun loginGoogle() {
        googleLoginManagerInstance.loginGoogle()
    }
}