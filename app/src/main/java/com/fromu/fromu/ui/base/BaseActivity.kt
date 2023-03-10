package com.fromu.fromu.ui.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.dialog.LoadingDialog
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.Utils

abstract class BaseActivity<T : ViewDataBinding>(private val inflate: (LayoutInflater) -> T) : AppCompatActivity() {
    protected lateinit var binding: T
    lateinit var loadingDialog: LoadingDialog

    private var isLoading: Boolean = false


    val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //권한 허용

            } else {
                //권한 거부
                //TODO 추후 한 번 더 확인용 다이얼로그 디자인 받기
                Utils.showCustomSnackBar(binding.root, "알림 허용을 원하시면 마이페이지에서 변경하실 수 있습니다.")
            }
        }

    val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //권한 허용

            } else {
                //권한 거부
                //TODO 추후 한 번 더 확인용 다이얼로그 디자인 받기
            }
        }


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
    fun showLoadingDialog() {
        if (!isLoading) {
            loadingDialog = LoadingDialog(this)
            loadingDialog.show()
            isLoading = true
        }
    }

    /**
     * 출력된 로딩 다이얼로그를 해제하기 위한 메소드
     */
    fun dismissLoadingDialog() {
        if (isLoading) {
            if (loadingDialog.isShowing) {
                loadingDialog.dismiss()
                isLoading = false
            }
        }
    }

    /**
     * Resource 핸들링
     *
     * @param T : API Response
     * @param resource
     * @param listener
     */
    open fun <T> handleResource(resource: Resource<T>, isShowLoadingDialog: Boolean = false, listener: ResourceSuccessListener<T>) {
        when (resource) {
            is Resource.Loading -> {
                if (isShowLoadingDialog)
                    showLoadingDialog()
            }
            is Resource.Success -> {
                dismissLoadingDialog()
                listener.onSuccess(resource.body)
            }
            is Resource.Failed -> {
                dismissLoadingDialog()
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    /**
     * sdk 33이상을 위한 백 프레스드
     *
     * @param callback
     */
    fun backPressed(callback: OnBackPressedCallback) {
        this.onBackPressedDispatcher.addCallback(callback)
    }

    /**
     * 알림 권한 체크
     *
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val notificationCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
        if (notificationCheck != PackageManager.PERMISSION_DENIED) {
            //권한 있음
            //Nothing
            Logger.e("checkNotificationPermission", "NotificationStorage권한 있음")
        } else {
            //권한 없음
            Logger.e("checkNotificationPermission", "NotificationStorage권한 없음")
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * 저장소 읽기 권한 체크
     *
     */
    fun checkReadStoragePermission() {
        val notificationCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (notificationCheck != PackageManager.PERMISSION_DENIED) {
            //권한 있음
            //Nothing
        } else {
            //권한 없음
            Logger.e("checkNotificationPermission", "ReadStorage권한 없음")
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}