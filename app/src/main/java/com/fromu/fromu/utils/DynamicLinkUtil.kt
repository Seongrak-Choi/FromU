package com.fromu.fromu.utils

import android.app.Activity
import android.net.Uri
import com.fromu.fromu.model.listener.DynamicLinkListener
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicLinkUtil @Inject constructor() {
    companion object {
        const val INVITATION_CODE_SCHEME = "invitationCodeShare"
        const val INVITATION_CODE_KEY = "invitationCode"

        private const val DYNAMIC_LINK = "https://fromu.com/"
        const val DYNAMIC_SHORTEN_URL = "https://fromu.page.link"
    }

    /**
     * 초대 코드 공유하기 용 링크 생성 메소드
     **
     * @return
     */
    fun getInvitationDeepLink(invitationCode: String): Uri {
        return Uri.parse("$DYNAMIC_LINK$INVITATION_CODE_SCHEME/?$INVITATION_CODE_KEY=$invitationCode")
    }

    /**
     * 다이나믹 링크 생성
     *
     * @param activity
     * @param deepLink
     * @param listener
     */
    fun getDynamicLink(activity: Activity, deepLink: Uri, listener: DynamicLinkListener) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(deepLink)
            .setDomainUriPrefix(DYNAMIC_SHORTEN_URL)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(activity.packageName)
                    .setMinimumVersion(1)
                    .build()
            )
            .buildShortDynamicLink()
            .addOnCompleteListener(activity) {
                if (it.isSuccessful) {
                    val shortLink: Uri = it.result.shortLink!!
                    listener.onSuccess(shortLink.toString())
                } else {
                    Logger.e("dynamicLink", "onDynamicLinkClick: 다이나믹 링크 생성 실패")
                    listener.onFailure()
                }
            }
    }
}
