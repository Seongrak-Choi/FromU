package com.fromu.fromu.ui.mailbox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PatchMailBoxNameReq
import com.fromu.fromu.data.remote.network.response.PatchMailBoxNameRes
import com.fromu.fromu.databinding.ActivityDecideMailBoxNameBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.ui.main.MainActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.utils.Extension.setThrottleClick
import com.fromu.fromu.utils.UiUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.DecideMailBoxNameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.ceil

@AndroidEntryPoint
class DecideMailBoxNameActivity : BaseActivity<ActivityDecideMailBoxNameBinding>(ActivityDecideMailBoxNameBinding::inflate), Observer<Resource<PatchMailBoxNameRes>> {

    private val decideMailBoxNameViewModel: DecideMailBoxNameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {}
    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)

        binding.apply {
            lifecycleOwner = this@DecideMailBoxNameActivity
            vm = decideMailBoxNameViewModel
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {

            etContents.apply {
                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    vDecideMailBoxUnderline.isSelected = hasFocus
                }

                debounce(coroutineScope = lifecycleScope) {
                    if (it.isEmpty()) {
                        decideMailBoxNameViewModel.isInvalidMailBoxName.value = false
                    } else {
                        if (checkPattern(it)) {
                            decideMailBoxNameViewModel.isInvalidMailBoxName.value = true
                            setNicknameNormalUi()
                        } else {
                            decideMailBoxNameViewModel.isInvalidMailBoxName.value = false
                            setNicknameErrorUi(getString(R.string.decide_mail_box_name_invalid_error_msg))
                        }
                    }
                }

                doAfterTextChanged {
                    if (it.toString().isEmpty()) {
                        tvDecideMailBoxContextSuffix.visibility = View.GONE
                        setResizeEditTextWidthByTextWidth(this, getString(R.string.decide_mail_box_name_hint)) //hint ?????? ?????? width ??????
                    } else {
                        tvDecideMailBoxContextSuffix.visibility = View.VISIBLE
                        setResizeEditTextWidthByTextWidth(this, it.toString())
                    }

                    decideMailBoxNameViewModel.isInvalidMailBoxName.value = false

                    invalidate()
                    setNicknameNormalUi()
                }
            }


            // ???????????? ??????
            tvDecideMailBoxConnect.setThrottleClick(lifecycleScope) {
                lifecycleScope.launch {
                    decideMailBoxNameViewModel.patchMailBoxName(PatchMailBoxNameReq("${etContents.text.toString()}???")).observe(this@DecideMailBoxNameActivity, this@DecideMailBoxNameActivity)
                }
            }
        }
    }

    /**
     * ????????? Text??? ???????????? width??? ?????? ????????? ?????????
     *
     * @param editText
     * @param text
     */
    private fun setResizeEditTextWidthByTextWidth(editText: EditText, text: String) {
        val textWidth: Float = editText.paint.measureText(text)
        val width = ceil(textWidth.toDouble()).toInt()
        val param = LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        editText.layoutParams = param
    }

    /**
     * ????????? ????????? ????????? ?????? ?????????
     *
     * @param str
     * @return
     */
    private fun checkPattern(str: String): Boolean {
        return str.matches(Regex(Const.NO_SPECIAL_CHAR_AND_NO_GAP_EXPRESSION))
    }


    /**
     * ???????????? ???????????? ??? ?????? UI ??????
     *
     */
    private fun setPassNicknameUi() {
        binding.apply {
            vDecideMailBoxUnderline.background = ContextCompat.getDrawable(this@DecideMailBoxNameActivity, R.color.color_dedee2)
            tvWarringMsg.visibility = View.GONE
        }
    }


    /**
     * ???????????? ???????????? ?????? ????????? ?????? UI ??????
     *
     */
    private fun setNicknameErrorUi(errorMsg: String) {
        binding.apply {
            vDecideMailBoxUnderline.background = ContextCompat.getDrawable(this@DecideMailBoxNameActivity, R.color.color_ff4a6b)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.text = errorMsg
            tvWarringMsg.setTextColor(ContextCompat.getColor(this@DecideMailBoxNameActivity, R.color.color_ff4a6b))
        }
    }


    /**
     *  ???????????? ????????? ?????? UI ??????
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vDecideMailBoxUnderline.background = ContextCompat.getDrawable(this@DecideMailBoxNameActivity, R.color.color_a735ff)
            tvWarringMsg.visibility = View.VISIBLE
            tvWarringMsg.setTextColor(ContextCompat.getColor(this@DecideMailBoxNameActivity, R.color.color_6f6f6f))
        }
    }

    private fun handlePatchMailBoxNameRes(res: PatchMailBoxNameRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
                finish()
            }
            2062 -> {
                setNicknameErrorUi(getString(R.string.decide_mail_box_name_duplication_error_msg))
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    override fun onChanged(resource: Resource<PatchMailBoxNameRes>) {
        handleResource(resource, listener = object : ResourceSuccessListener<PatchMailBoxNameRes> {
            override fun onSuccess(res: PatchMailBoxNameRes) {
                handlePatchMailBoxNameRes(res)
            }
        })
    }
}