package com.fromu.fromu.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.fromu.fromu.databinding.ActivitySplashBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.DynamicLinkUtil
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.SplashViewModel
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

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
        binding.apply {

        }
    }


    /**
     * dynamic link handle method
     */
    private fun handleDynamicLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { data ->
                if (data == null) {
                    // 정상적으로 앱을 켰을 때
                    //TODO 자동 로그인 실행
                    goLoginActivityWithDelay()
                } else {
                    // 다이나믹 링크로 앱이 켜졌을 때
                    val deepLink: Uri? = data.link
                    Logger.e("rak", "다이나믹 링크로 켜짐")

                    if (deepLink != null) {
                        Logger.e("rak", "deepLink 널 아님")
                        Logger.e("rak", "${deepLink.lastPathSegment}")

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
            }
    }


    /**
     * 초대 코드의 다이나믹링크로 접속한 경우 셋팅
     *
     * @param deepLink
     */
    private fun initInvitationCode(deepLink: Uri) {
        val invitationCode = deepLink.getQueryParameter(DynamicLinkUtil.INVITATION_CODE_KEY) ?: ""
        Logger.e("rak", invitationCode)
        // TODO 자동 로그인 실행
        Utils.setTextToClipboard(this, invitationCode)

        goLoginActivityWithDelay()
    }

    private fun goLoginActivityWithDelay() {
        lifecycleScope.launch {
            delay(2000)
            Intent(this@SplashActivity, LoginActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}