package com.fromu.fromu.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.LoginRes
import com.fromu.fromu.databinding.ActivityLoginBinding
import com.fromu.fromu.model.LoginType
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.ui.invitaion.InvitationActivity
import com.fromu.fromu.ui.mailbox.DecideMailBoxNameActivity
import com.fromu.fromu.ui.signup.SignupActivity
import com.fromu.fromu.ui.signup.SignupActivity.Companion.EMAIL_KEY
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
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), Observer<Resource<LoginRes>> {


    //google 로그인 callback
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var googleLoginManagerInstance: GoogleLoginManager

    private lateinit var kakaoLoginManager: KakaoLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        initLauncher()
        googleLoginManagerInstance = GoogleLoginManager(this, activityLauncher)
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)
        binding.apply {
            lifecycleOwner = this@LoginActivity
            view = this@LoginActivity
        }
    }

    private fun initLauncher() {
        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                googleLoginManagerInstance.handleSignInResult(task)?.let { accessToken ->
                    //서버로 accessToken 전달
                    lifecycleScope.launch {
                        loginViewModel.login(accessToken, LoginType.GOOGLE).observe(this@LoginActivity, this@LoginActivity)
                    }

                } ?: let {
                    Utils.showCustomSnackBar(binding.root, "구글 로그인에 실패하였습니다.")
                }
            } else {
                Utils.showCustomSnackBar(binding.root, "구글 로그인에 실패하였습니다.")
            }
        }
    }


    /**
     * 카카오 로그인
     */
    fun loginKakao() {
        kakaoLoginManager = KakaoLoginManager(this)

        kakaoLoginManager.loginKakao(object : KakaoLoginManager.OnKakaoLoginListener {
            override fun onSuccess(accessToken: String) {
                Logger.d("kakao_login", accessToken)
                lifecycleScope.launch {
                    loginViewModel.login(accessToken, LoginType.KAKAO).observe(this@LoginActivity, this@LoginActivity)
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

    /**
     * 로그인 결과 핸들링
     */
    private fun handleLoginResult(loginRes: LoginRes) {
        when (loginRes.code) {
            Const.SUCCESS_CODE -> {
                if (loginRes.result.isMember) { //회원인 경우
                    loginRes.result.userInfo?.let { userInfo ->
                        if (userInfo.isMatch) { //커플 매칭이 된 경우
                            if (userInfo.isSetMailboxName) { //우편함 이름을 정한 경우
                                Intent(this, MainActivity::class.java).apply {
                                    startActivity(this)
                                }
                                finish()
                            } else {
                                Intent(this, DecideMailBoxNameActivity::class.java).apply {
                                    startActivity(this)
                                }
                                finish()
                            }
                        } else { //커플 매칭이 안 된 경우
                            Intent(this, InvitationActivity::class.java).apply {
                                startActivity(this)
                            }
                            finish()
                        }
                    }
                } else { //회원이 아닌 경우
                    Intent(this, SignupActivity::class.java).apply {
                        putExtra(EMAIL_KEY, FromUApplication.prefManager.getUserLoginEmail())
                        startActivity(this)
                    }
                    finish()
                }
            }
            else -> {
                Utils.showCustomSnackBar(binding.root, "로그인에 실패하였습니다.")
            }
        }
    }

    override fun onChanged(resource: Resource<LoginRes>) {
        handleResource(resource, true, object : ResourceSuccessListener<LoginRes> {
            override fun onSuccess(res: LoginRes) {
                handleLoginResult(res)
            }
        })
    }
}