package com.fromu.fromu.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Utils {

    companion object {
        /**
         * 릴리즈 용 shq1에 해당하는 해시키를 반환하는 메소드
         *
         * @param context
         * @return
         */
        fun getReleaseKeyHashBase64(context: Context): String? {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES) ?: return null
            for (signature in packageInfo.signatures) {
                try {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    return Base64.encodeToString(md.digest(), Base64.DEFAULT)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }
}