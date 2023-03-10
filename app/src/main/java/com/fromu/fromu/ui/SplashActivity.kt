package com.fromu.fromu.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
                if (data == null) { // ??????????????? ?????? ?????? ???
                    runWhenNormalStart()
                } else { // ???????????? ????????? ?????? ????????? ???
                    runWhenDynamicLinkStart(data)
                }
            }
    }


    /**
     * ?????? ????????? ????????????????????? ????????? ?????? ??????
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
     * ???????????? ????????? ?????? ?????? ??? ?????? ??? ??????
     *
     * @param data
     */
    private fun runWhenDynamicLinkStart(data: PendingDynamicLinkData) {
        val deepLink: Uri? = data.link

        if (deepLink != null) {
            when (deepLink.lastPathSegment) {
                DynamicLinkUtil.INVITATION_CODE_SCHEME -> {
                    // ?????? ?????? ?????? ????????? ??????
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
     * ??????????????? ?????? ?????? ?????? ?????? ?????? ??? ??????
     *
     */
    private fun runWhenNormalStart() {
        val jwt = FromUApplication.prefManager.getLoginToken()

        if (jwt.isNullOrEmpty()) { //????????? jwt??? ????????? ????????? ???????????? ??????
            checkInitOnBoardingBeforeGoingLoginActivity()
        } else { //????????? jwt??? ????????? ??????????????? api ??????
            loginViewModel.loginWithRefreshToken()
        }
    }

    /**
     * ???????????? ??? ?????? ????????? ????????? ???????????? ????????? LoginActivity, ????????? OnBoardingActivity??? ??????
     *
     */
    private fun checkInitOnBoardingBeforeGoingLoginActivity() {
        if (!FromUApplication.prefManager.sp.getBoolean(PrefManager.INIT_ON_BOARDING_KEY, false)) //????????? ??? ??? ????????? ??????
            goActivityWithDelay(OnBoardingActivity::class.java)
        else
            goActivityWithDelay(LoginActivity::class.java)
    }


    private fun handleLoginWithRefreshToken(res: JWTLoginRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                res.result?.let { userInfo ->
                    if (userInfo.isMatch) { //?????? ????????? ??? ??????
                        if (userInfo.isSetMailboxName) { //????????? ????????? ?????? ??????
                            goActivityWithDelay(MainActivity::class.java)
                        } else {
                            goActivityWithDelay(DecideMailBoxNameActivity::class.java)
                        }
                    } else { //?????? ????????? ??? ??? ??????
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
}