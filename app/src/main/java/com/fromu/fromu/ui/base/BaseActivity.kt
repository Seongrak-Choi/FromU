package com.fromu.fromu.ui.base

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.fromu.fromu.utils.LoadingDialog

abstract class BaseActivity<T : ViewDataBinding>(private val inflate: (LayoutInflater) -> T) : AppCompatActivity() {
    protected lateinit var binding: T
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStop() {
        super.onStop()

        binding.unbind()
    }


    /**
     * 키보드를 강제로 내리기 위한 메소드
     */
    protected fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = window.currentFocus
        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    /**
     * 진동 울리는 메소드
     *
     * @param milliSecondsOfDuration
     * @param power
     */
    fun doVibrate(milliSecondsOfDuration: Long, power: Int) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(milliSecondsOfDuration, power))
    }

    /**
     * 로딩 다이얼로그를 출력하기 위한 메소드
     *
     * @param context
     */
    fun showLoadingDialog(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.show()
    }

    /**
     * 출력된 로딩 다이얼로그를 해제하기 위한 메소드
     */
    fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}