package com.fromu.fromu.ui.main.diary.inside

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fromu.fromu.R
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.WriteDiaryRes
import com.fromu.fromu.databinding.FragmentAddInsideDiaryBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.base.ImgCropActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.KeyboardVisibilityUtils
import com.fromu.fromu.utils.TimeUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.AddInsideDairyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddInsideDiaryFragment : BaseFragment<FragmentAddInsideDiaryBinding>(FragmentAddInsideDiaryBinding::inflate), Observer<Resource<WriteDiaryRes>> {
    private val addInsideDiaryViewModel: AddInsideDairyViewModel by viewModels()

    // 키보드 show에 따른 스크롤 위치 변경 유틸 클래스
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    // 갤러리 콜백 결과
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>

    // 이미지 크롭 결과
    private lateinit var cropImgLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initData() {
        initGalleryResultLauncher()
        initCropImgLauncher()
        settingDate()
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@AddInsideDiaryFragment
            vm = addInsideDiaryViewModel
        }

        initKeyBoardVisibilityUtils()
        initEvent()
        initObserve()
    }


    private fun initEvent() {
        binding.apply {
            //사진
            vImgArea.setOnClickListener {
                Utils.goGalleryWithSinglePicture(galleryResultLauncher)
            }

            // 완료
            tvAddInsideDiaryDone.setOnClickListener {
                lifecycleScope.launch {
                    addInsideDiaryViewModel.writeDiary().observe(viewLifecycleOwner, this@AddInsideDiaryFragment)
                }
            }

            // 뒤로가기
            ivAddInsideDiaryBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserve() {
        addInsideDiaryViewModel.apply {
            lifecycleScope.launch {
                checkWeatherValue.collect {
                    checkInvalidInputData()
                }
            }

            lifecycleScope.launch {
                cropImgPath.collect {
                    checkInvalidInputData()
                }
            }

            lifecycleScope.launch {
                inputContents.collect {
                    checkInvalidInputData()
                }
            }
        }
    }

    /**
     * 오늘의 요일 셋팅
     */
    private fun settingDate() {
        addInsideDiaryViewModel.apply {
            dayOfToday.value = TimeUtils.getToday().toString()
            monthOfToday.value = TimeUtils.getMonthOfToday().toString()
            dayOfWeek.value = TimeUtils.getDayOfWeek()
        }
    }

    /**
     * 갤러리 결과 셋팅
     */
    private fun initGalleryResultLauncher() {
        galleryResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val uri = result.data?.data

            uri?.let {
                Intent(requireContext(), ImgCropActivity::class.java).apply {
                    putExtra(ImgCropActivity.GALLERY_PICK_IMG, it.toString())
                    cropImgLauncher.launch(this)
                }
            } ?: let {
                Utils.showCustomSnackBar(binding.root, getString(R.string.load_img_failure_msg))
            }
        }
    }


    /**
     * 이미지 크롭 결과 셋팅
     */
    private fun initCropImgLauncher() {
        cropImgLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ImgCropActivity.CROP_RESULT_CODE) {
                result.data?.let {
                    val cropImageFilePath = it.getStringExtra(ImgCropActivity.CROP_IMG_FILE_PATH)

                    addInsideDiaryViewModel.cropImgPath.value = cropImageFilePath.toString()
                    setCropImg(cropImageFilePath)
                }
            } else {
                Utils.showCustomSnackBar(binding.root, getString(R.string.load_img_failure_msg))
            }
        }
    }


    /**
     * 키보드 높이 만큼 스크롤을 내려서 입력창을 보이도록 셋팅
     */
    private fun initKeyBoardVisibilityUtils() {
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window, onShowKeyboard = { keyboardHeight ->
            binding.nsvAddInsideDiary.run {
                smoothScrollTo(scrollX, scrollY + keyboardHeight)
            }
        })
    }


    /**
     * crop된 이미지를 셋팅
     *
     * @param cropImgFilePath: String = "크롭된 이미지 파일 경로"
     */
    private fun setCropImg(cropImgFilePath: String?) {
        val cropImgBitmap: Bitmap? = BitmapFactory.decodeFile(cropImgFilePath)

        Glide.with(binding.ivAddInsideDiaryImg)
            .load(cropImgBitmap)
            .into(binding.ivAddInsideDiaryImg)
    }


    private fun handleWriteDiaryRes(res: WriteDiaryRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("key", "value that needs to be passed")
                findNavController().popBackStack()
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }


    override fun onDestroyView() {
        keyboardVisibilityUtils.detachKeyboardListeners()

        super.onDestroyView()
    }

    override fun onChanged(resource: Resource<WriteDiaryRes>) {
        handleResource(resource, true, listener = object : ResourceSuccessListener<WriteDiaryRes> {
            override fun onSuccess(res: WriteDiaryRes) {
                handleWriteDiaryRes(res)
            }
        })
    }
}