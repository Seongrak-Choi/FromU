package com.fromu.fromu.ui.base

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.dialog.LoadingDialog
import com.fromu.fromu.utils.Logger
import com.fromu.fromu.utils.Utils


abstract class BaseFragment<T : ViewDataBinding>(private val inflate: (LayoutInflater) -> T) : Fragment() {
    protected lateinit var binding: T
    lateinit var loadingDialog: LoadingDialog

    private var isLoading: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        dismissLoadingDialog()
    }


    /**
     * 키보드를 강제로 내리기 위한 메소드
     */
    protected fun hideKeyboard() {
        val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = requireActivity().window.currentFocus
        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 키보드를 올리기 위한 메소드
     */
    protected fun openKeyboard(et: EditText) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 토스트 메세지를 간편하게 출력하기 위한 메소드
     *
     * @param message = 출력하고자 하는 문자열
     */
    fun showCustomToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }


    /**
     * 진동 울리는 메소드
     *
     * @param milliSecondsOfDuration
     * @param power 기본 100
     */
    fun doVibrate(milliSecondsOfDuration: Long, power: Int = 100) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Logger.e("rak", "진동하기")
            val vibratorManager = requireActivity().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrationEffect = VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE)
            val combinedVibration = CombinedVibration.createParallel(vibrationEffect)
            vibratorManager.vibrate(combinedVibration)
        } else {
            @Suppress("DEPRECATION")
            val vibrator = requireActivity().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(milliSecondsOfDuration, power))
        }
    }

    /**
     * 로딩 다이얼로그를 출력하기 위한 메소드
     *
     * @param context
     */
    fun showLoadingDialog() {
        if (!isLoading) {
            loadingDialog = LoadingDialog(requireContext())
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
}