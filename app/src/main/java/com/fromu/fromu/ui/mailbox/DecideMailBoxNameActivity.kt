package com.fromu.fromu.ui.mailbox

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.fromu.fromu.R
import com.fromu.fromu.databinding.ActivityDecideMailBoxNameBinding
import com.fromu.fromu.ui.base.BaseActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Extension.debounce
import com.fromu.fromu.utils.Extension.setThrottleClick
import com.fromu.fromu.viewmodels.DecideMailBoxNameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class DecideMailBoxNameActivity : BaseActivity<ActivityDecideMailBoxNameBinding>(ActivityDecideMailBoxNameBinding::inflate) {

    private val decideMailBoxNameViewModel: DecideMailBoxNameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {}
    private fun initView() {
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
                            setNicknameErrorUi()
                        }
                    }
                }

                doAfterTextChanged {
                    if (it.toString().isEmpty()) {
                        tvDecideMailBoxContextSuffix.visibility = View.GONE
                        setResizeEditTextWidthByTextWidth(this, getString(R.string.decide_mail_box_name_hint)) //hint 길이 만큼 width 설정
                    } else {
                        tvDecideMailBoxContextSuffix.visibility = View.VISIBLE
                        setResizeEditTextWidthByTextWidth(this, it.toString())
                    }

                    decideMailBoxNameViewModel.isInvalidMailBoxName.value = false

                    invalidate()
                    setNicknameNormalUi()
                }
            }

            // 중복 확인 버튼
            tvDecideMailBoxCheckDuplication.setThrottleClick(lifecycleScope) {
                //TODO 중복확인 api 호출
            }
        }
    }

    /**
     * 입력한 Text의 길이만큼 width를 다시 그리는 메소드
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
     * 입력한 닉네임 정규화 확인 메소드
     *
     * @param str
     * @return
     */
    private fun checkPattern(str: String): Boolean {
        return str.matches(Regex(Const.NOT_SPECIAL_CHAR_EXPRESSION))
    }

    /**
     * 정규화에 일치하지 않은 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameErrorUi() {
        binding.apply {
            vDecideMailBoxUnderline.background = ContextCompat.getDrawable(this@DecideMailBoxNameActivity, R.color.color_ff4a6b)
            tvWarringMsg.setTextColor(ContextCompat.getColor(this@DecideMailBoxNameActivity, R.color.color_ff4a6b))
        }
    }

    /**
     *  일반적인 경우의 입력 UI 셋팅
     *
     */
    private fun setNicknameNormalUi() {
        binding.apply {
            vDecideMailBoxUnderline.background = ContextCompat.getDrawable(this@DecideMailBoxNameActivity, R.color.color_a735ff)
            tvWarringMsg.setTextColor(ContextCompat.getColor(this@DecideMailBoxNameActivity, R.color.color_6f6f6f))
        }
    }

}