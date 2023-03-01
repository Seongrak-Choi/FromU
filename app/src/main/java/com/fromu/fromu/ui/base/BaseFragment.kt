package com.fromu.fromu.ui.base

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.utils.LoadingDialog
import com.fromu.fromu.utils.Utils

abstract class BaseFragment<T : ViewDataBinding>(private val inflate: (LayoutInflater) -> T) : Fragment() {
    protected lateinit var binding: T
    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflate(layoutInflater)
        return binding.root
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
     * 진동 울리는 메소드
     *
     * @param milliSecondsOfDuration
     * @param power
     */
    fun doVibrate(milliSecondsOfDuration: Long, power: Int) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = requireActivity().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireActivity().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(milliSecondsOfDuration, power))
    }

    /**
     * 로딩 다이얼로그를 출력하기 위한 메소드
     *
     * @param context
     */
    fun showLoadingDialog() {
        loadingDialog = LoadingDialog(requireContext())
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