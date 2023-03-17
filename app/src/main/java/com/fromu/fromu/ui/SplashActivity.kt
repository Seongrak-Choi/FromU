package com.fromu.fromu.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.data.remote.network.response.JWTLoginRes
import com.fromu.fromu.databinding.ActivitySplashBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.ui.invitaion.InvitationActivity
import com.fromu.fromu.ui.mailbox.DecideMailBoxNameActivity
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.ui.onboarding.OnBoardingActivity
import com.fromu.fromu.utils.*
import com.fromu.fromu.viewmodels.LoginViewModel
import com.fromu.fromu.viewmodels.SplashViewModel
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initData()
        initView()
        initBackPress()
    }

    private fun initData() {
        handleDynamicLinks()
    }

    private fun initView() {
        UiUtils.hideStatusBarAndSystemBar(window)
        UiUtils.setFullScreenWithStatusBar(this)

        initEvent()
    }

    private fun initEvent() {
        loginViewModel.loginWithRefreshTokenResult.observe(this) { resource ->
            handleResource(resource, listener = object : ResourceSuccessListener<JWTLoginRes> {
                override fun onSuccess(res: JWTLoginRes) {
                    handleLoginWithRefreshToken(res)
                }
            })
        }
    }


    /**
     * dynamic link handle method
     */
    private fun handleDynamicLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { data ->
                if (data == null) { // 정상적으로 앱을 켰을 때
                    runWhenNormalStart()
                } else { // 다이나믹 링크로 앱이 켜졌을 때
                    runWhenDynamicLinkStart(data)
                }
            }
    }


    /**
     * 초대 코드의 다이나믹링크로 접속한 경우 셋팅
     *
     * @param deepLink
     */
    private fun initInvitationCode(deepLink: Uri) {
        val invitationCode = deepLink.getQueryParameter(DynamicLinkUtil.INVITATION_CODE_KEY) ?: ""

        Utils.setTextToClipboard(this, invitationCode)
        runWhenNormalStart()
    }

    private fun <T> goActivityWithDelay(activity: Class<T>) {
        lifecycleScope.launch {
            delay(3000)
            Intent(this@SplashActivity, activity).apply {
                startActivity(this)
                finish()
            }
        }
    }


    /**
     * 다이나믹 링크로 앱을 켰을 때 수행 할 작업
     *
     * @param data
     */
    private fun runWhenDynamicLinkStart(data: PendingDynamicLinkData) {
        val deepLink: Uri? = data.link

        if (deepLink != null) {
            when (deepLink.lastPathSegment) {
                DynamicLinkUtil.INVITATION_CODE_SCHEME -> {
                    // 초대 코드 공유 링크인 경우
                    initInvitationCode(deepLink)
                }
                else -> {
                    Logger.e("handleDynamicLink", "no scheme")
                }
            }
        } else {
            Logger.e("handleDynamicLink", "getDynamicLink: no link found")
        }
    }

    /**
     * 일반적으로 앱을 실행 했을 경우 수행 할 작업
     *
     */
    private fun runWhenNormalStart() {
        val jwt = FromUApplication.prefManager.getLoginToken()

        if (jwt.isNullOrEmpty()) { //저장된 jwt가 없다면 로그인 화면으로 이동
            checkInitOnBoardingBeforeGoingLoginActivity()
        } else { //저장된 jwt가 있다면 자동로그인 api 호출
            loginViewModel.loginWithRefreshToken()
        }
    }

    /**
     * 온보딩을 본 적이 있는지 없는지 체크해서 있으면 LoginActivity, 없으면 OnBoardingActivity로 이동
     *
     */
    private fun checkInitOnBoardingBeforeGoingLoginActivity() {
        if (!FromUApplication.prefManager.sp.getBoolean(PrefManager.INIT_ON_BOARDING_KEY, false)) //온보딩 본 적 있는지 체크
            goActivityWithDelay(OnBoardingActivity::class.java)
        else
            goActivityWithDelay(LoginActivity::class.java)
    }


    private fun handleLoginWithRefreshToken(res: JWTLoginRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                res.result?.let { userInfo ->
                    if (userInfo.isMatch) { //커플 매칭이 된 경우
                        if (userInfo.isSetMailboxName) { //우편함 이름을 정한 경우
                            goActivityWithDelay(MainActivity::class.java)
                        } else {
                            goActivityWithDelay(DecideMailBoxNameActivity::class.java)
                        }
                    } else { //커플 매칭이 안 된 경우
                        goActivityWithDelay(InvitationActivity::class.java)
                    }
                }
            }
            4000 -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
            else -> {
                checkInitOnBoardingBeforeGoingLoginActivity()
            }
        }
    }


    private fun initBackPress() {
        backPressed(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Utils.exitApp(this@SplashActivity)
            }
        })
    }
}