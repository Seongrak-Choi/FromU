package com.fromu.fromu.utils

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.fromu.fromu.R
import com.fromu.fromu.model.OnDiaryCropImgListener
import com.fromu.fromu.utils.custom.FromUBigSnackBarBlack
import com.fromu.fromu.utils.custom.FromUSnackBarBlack
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.system.exitProcess


class Utils {

    companion object {
        /**
         * 대입한 sha1에 해당한 해시키 반환하는 메소드
         *
         * @param sha1
         */
        fun getKeyHash(sha1: String): String {
            val sha1Arr = sha1.split(':')
            var byteArr = byteArrayOf()
            for (hex in sha1Arr) {
                byteArr += Integer.parseInt(hex, 16).toByte()
            }

            return Base64.encodeToString(byteArr, Base64.NO_WRAP)
        }


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

        /**
         * show FromUCustomSnackBar method
         *
         * @param view
         * @param message
         * @param duration
         */
        fun showCustomSnackBar(view: View, message: String, duration: Int = 2000) {
            FromUSnackBarBlack(view, message, duration).show()
        }

        /**
         * show FromUBigCustomSnackBar method
         *
         * @param view
         * @param message
         * @param duration
         */
        fun showBigCustomSnackBar(view: View, message: String, duration: Int = 2000) {
            FromUBigSnackBarBlack(view, message, duration).show()
        }

        /**
         * show networkErrorMsg
         *
         * @param view
         */
        fun showNetworkErrorSnackBar(view: View) {
            showCustomSnackBar(view, view.resources.getString(R.string.network_error))
        }

        /**
         * 공유하기
         *
         * @param activity
         * @param msg : String = 공유할 메시지
         * @param listener : SendStringListener = 결과 리스너
         */
        fun sendString(activity: FragmentActivity, msg: String) {
            try {
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, msg)
                    type = "text/plain"
                    activity.startActivity(Intent.createChooser(this, "Share"))
                }
            } catch (e: ActivityNotFoundException) {
                Logger.e("sendString", "공유하기 실패")
            }
        }


        /**
         * 클립보드에 String 복사
         */
        fun setTextToClipboard(context: Context, message: String) {
            val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copyMsg", message)

            clipboard.setPrimaryClip(clip)
        }

        /**
         * dp를 px로 변경해주는 메소드
         */
        fun dp2px(resources: Resources, dp: Float): Int {
            val metrics = resources.displayMetrics
            return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        }

        /**
         * px를 dp로 변경해주는 메소드
         */
        fun px2dp(resources: Resources, px: Float): Float {
            val metrics = resources.displayMetrics
            return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        /**
         * 앱을 종료하는 메소드
         */
        fun exitApp(activity: FragmentActivity) {
            ActivityCompat.finishAffinity(activity)
            System.runFinalization()
            exitProcess(0)
        }


        /**
         * 다이얼로그 팝업창 기기 화면 기준으로 가로 비율 설정
         *
         * @param percentage
         */
        fun DialogFragment.setWidthPercent(percentage: Int) {
            val percent = percentage.toFloat() / 100
            val dm = Resources.getSystem().displayMetrics
            val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
            val percentWidth = rect.width() * percent
            dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        fun goGalleryWithSinglePicture(galleryResultLauncher: ActivityResultLauncher<Intent>, listener: OnDiaryCropImgListener? = null) {
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                galleryResultLauncher.launch(this)
            }
        }
    }
}