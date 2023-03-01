package com.fromu.fromu.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginResponse
import com.fromu.fromu.databinding.ActivityLoginBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.ui.signup.SignupActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.utils.sns.GoogleLoginManager
import com.fromu.fromu.utils.sns.KakaoLoginManager
import com.fromu.fromu.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), Observer<Resource<LoginResponse>> {


    //google 로그인 callback
    private var activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            googleLoginManagerInstance.handleSignInResult(task)?.let { accessToken ->
                //서버로 accessToken 전달
                lifecycleScope.launch {
                    loginViewModel.login(accessToken, Const.LoginType.GOOGLE).observe(this@LoginActivity, this@LoginActivity)
                }

            } ?: let {
                Utils.showCustomSnackBar(binding.root, "구글 로그인에 실패하였습니다.")
            }
        } else {
            Utils.showCustomSnackBar(binding.root, "구글 로그인에 실패하였습니다.")
        }
    }

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var googleLoginManagerInstance: GoogleLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        googleLoginManagerInstance = GoogleLoginManager(this, activityLauncher)
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
        binding.apply {
            lifecycleOwner = this@LoginActivity
            view = this@LoginActivity
        }
    }


    /**
     * 카카오 로그인
     */
    fun loginKakao() {
        KakaoLoginManager(this).loginKakao(object : KakaoLoginManager.OnKakaoLoginListener {
            override fun onSuccess(accessToken: String) {
                Logger.d("kakao_login", accessToken)

                lifecycleScope.launch {
                    loginViewModel.login(accessToken, Const.LoginType.KAKAO).observe(this@LoginActivity, this@LoginActivity)
                }
            }

            override fun onFailure(errorMsg: String) {
                Utils.showCustomSnackBar(binding.root, "카카오 로그인에 실패하였습니다.")
                Logger.e("kakao_login", errorMsg)
            }
        })
    }

    /**
     * 구글 로그인
     */
    fun loginGoogle() {
        googleLoginManagerInstance.loginGoogle()
    }

    private fun handleLoginResult(loginResponse: LoginResponse) {
        when (loginResponse.code) {
            Const.SUCCESS_CODE -> {
                if (loginResponse.result.isMember) {
                    //TODO 벨라가 응답 변수 추가해 주면 매칭이 되었는지 확인
                } else {
                    Intent(this, SignupActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
            else -> {
                Utils.showCustomSnackBar(binding.root, "로그인에 실패하였습니다.")
            }
        }
    }

    override fun onChanged(resource: Resource<LoginResponse>) {
        when (resource) {
            is Resource.Loading -> {
                showLoadingDialog(this)
            }
            is Resource.Success -> {
                dismissLoadingDialog()
                handleLoginResult(resource.body)
            }
            is Resource.Failed -> {
                dismissLoadingDialog()
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}