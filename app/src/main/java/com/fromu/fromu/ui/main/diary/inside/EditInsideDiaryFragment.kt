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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DetailDiaryResult
import com.fromu.fromu.data.remote.network.response.DetailDiaryRes
import com.fromu.fromu.data.remote.network.response.EditDiaryRes
import com.fromu.fromu.databinding.FragmentEditInsideDiaryBinding
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.base.ImgCropActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.KeyboardVisibilityUtils
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.EditInsideDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditInsideDiaryFragment : BaseFragment<FragmentEditInsideDiaryBinding>(FragmentEditInsideDiaryBinding::inflate) {

    companion object {
        const val UPLOADED_DIARY_ID = "uploadedDiaryId"
    }

    private var diaryId: Int = 0 // 수정할 다이어리의 id

    private val editInsideDiaryViewModel: EditInsideDiaryViewModel by viewModels()

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
        initEvent()
        initObserve()
    }

    private fun initData() {
        getArgs()
        callDetailDiary()
        initGalleryResultLauncher()
        initCropImgLauncher()
    }

    private fun initView() {
        settingKeyBoardVisibilityUtils()

        binding.apply {
            lifecycleOwner = this@EditInsideDiaryFragment
            vm = editInsideDiaryViewModel
        }
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
                    editInsideDiaryViewModel.editDiary(diaryId)
                }
            }

            // 뒤로가기
            ivAddInsideDiaryBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserve() {
        editInsideDiaryViewModel.apply {
            lifecycleScope.launch {
                checkWeatherValue.collect {
                    checkInvalidInputData()
                }
            }

            lifecycleScope.launch {
                inputContents.collect {
                    checkInvalidInputData()
                }
            }


            getDetailDiaryResult.observe(viewLifecycleOwner, EventObserver { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<DetailDiaryRes> {
                    override fun onSuccess(res: DetailDiaryRes) {
                        handleGetDetailDiaryRes(res)
                    }
                })
            })

            editDiaryResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<EditDiaryRes> {
                    override fun onSuccess(res: EditDiaryRes) {
                        handleEditDiaryRes(res)
                    }
                })

            }
        }
    }

    private fun getArgs() {
        val args: EditInsideDiaryFragmentArgs by navArgs()
        diaryId = args.diaryId
    }

    /**
     * 일기 상세 정보 조회 api 호출
     *
     */
    private fun callDetailDiary() {
        editInsideDiaryViewModel.getDetailDiariesById(diaryId)
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

                    editInsideDiaryViewModel.cropImgPath.value = cropImageFilePath.toString()
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
    private fun settingKeyBoardVisibilityUtils() {
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

    /**
     * 기존에 작성했던 데이터를 UI에 셋팅
     *
     * @param detailDiary
     */
    private fun settingUi(detailDiary: DetailDiaryResult) {
        editInsideDiaryViewModel.apply {
            inputContents.value = detailDiary.content ?: ""
            checkWeatherValue.value = detailDiary.weather
            writeDate.value = detailDiary.date
            writeDayOfWeek.value = detailDiary.day
            Utils.setImgByUrl(binding.ivAddInsideDiaryImg, detailDiary.imageUrl)
        }
    }

    /**
     * 수정할 일기의 상세 정보 조회 결과 핸들링
     *
     * @param res
     */
    private fun handleGetDetailDiaryRes(res: DetailDiaryRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                //TODO 내가 작성한 일기가 아닌 경우 상대방이 작성한 일기라는 메시지 띄어주고 뒤로 백하자
                settingUi(res.result)
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }

    private fun handleEditDiaryRes(res: EditDiaryRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                findNavController().popBackStack()
                Utils.showCustomSnackBar(binding.root, "일기가 수정 되었어!")
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
}