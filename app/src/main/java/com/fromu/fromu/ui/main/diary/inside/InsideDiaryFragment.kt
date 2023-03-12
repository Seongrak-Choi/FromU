package com.fromu.fromu.ui.main.diary.inside

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DetailDiaryResult
import com.fromu.fromu.data.dto.DiaryBook
import com.fromu.fromu.data.remote.network.response.AllDiariesRes
import com.fromu.fromu.data.remote.network.response.ChangeFirstPageImgRes
import com.fromu.fromu.databinding.FragmentInsideDiaryBinding
import com.fromu.fromu.model.InsideDiaryModel
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.base.ImgCropActivity
import com.fromu.fromu.ui.main.diary.inside.index.IndexDiaryActivity
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.EventObserver
import com.fromu.fromu.utils.Extension.customGetSerializable
import com.fromu.fromu.utils.PrefManager
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.InsideDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InsideDiaryFragment : BaseFragment<FragmentInsideDiaryBinding>(FragmentInsideDiaryBinding::inflate) {
    private val insideDiaryViewModel: InsideDiaryViewModel by viewModels()

    // 일기장 viewPagerAdapter
    private lateinit var insideDiaryVpAdapter: InsideDiaryVpAdapter

    // 일기장 디테일 저장할 리스트
    private val insideDiaryList: ArrayList<InsideDiaryModel> = arrayListOf()

    // 갤러리 콜백 결과 리스너
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>

    // 이미지 크롭 결과 리스너
    private lateinit var cropImgLauncher: ActivityResultLauncher<Intent>

    // 선택한 이미지 파일 경로
    private var selectedCropImgFilePath: String? = null

    private var diaryBookInfo: DiaryBook? = null

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
        arguments?.let {
            //TODO 이 부분 데이터 넘겨 받는 것이 아닌, api로 값을 받도록 하자. 벨라가 writeFlag 추가해 주면!!!!
            diaryBookInfo = it.customGetSerializable(InsideDiaryActivity.DIARY_BOOK_ID_KEY)

            diaryBookInfo?.let { diaryBookInfo ->
                insideDiaryList.add(InsideDiaryModel.Header(diaryBookInfo))
                insideDiaryViewModel.diaryFirstPageFilePath.value = diaryBookInfo.imageUrl
            } ?: let {
                showCustomToast("다시 시도해 주세요.")
                requireActivity().finish()
            }
        }

        // 디스크립션 visible
        insideDiaryViewModel.isShowDescription.value = FromUApplication.prefManager.sp.getBoolean(PrefManager.WHETHER_SHOW_DIARY_DESCRIPTION, true)

        initGalleryResultLauncher()
        initCropImgLauncher()
        callApi()

        insideDiaryVpAdapter = InsideDiaryVpAdapter(childFragmentManager, insideDiaryViewModel, galleryResultLauncher)
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@InsideDiaryFragment
            vm = insideDiaryViewModel
        }
        settingViewPager()
    }

    private fun settingViewPager() {
        binding.vpInsideDiary.apply {
            adapter = insideDiaryVpAdapter
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            offscreenPageLimit = 5
        }
    }

    private fun initEvent() {
        binding.apply {
            // 일기 쓰기 버튼
            ivInsideDiaryAdd.setOnClickListener {
                findNavController().navigate(R.id.action_insideDiaryFragment_to_addInsideDiaryFragment)
            }

            // viewpager
            vpInsideDiary.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    insideDiaryViewModel.currentDiaryPosition.value = position
                }
            })

            // 이전 페이지 버튼
            ivInsideDiaryPositionBack.setOnClickListener {
                //TODO 추후 페이징 생각하면 로직 변경 해야 됨
                if (vpInsideDiary.currentItem != 0)
                    vpInsideDiary.currentItem = vpInsideDiary.currentItem - 1
            }

            // 다음 페이지 버튼
            ivInsideDiaryPositionFront.setOnClickListener {
                //TODO 추후 페이징 생각하면 로직 변경 해야 됨
                if (vpInsideDiary.currentItem != insideDiaryVpAdapter.currentList.size - 1)
                    vpInsideDiary.currentItem = vpInsideDiary.currentItem + 1
            }

            // 마지막 페이지 버튼
            tvInsideDiaryGoLastPage.setOnClickListener {
                vpInsideDiary.currentItem = insideDiaryVpAdapter.currentList.size - 1
            }

            // 백 버튼
            ivInsideDiaryBack.setOnClickListener {
                requireActivity().finish()
            }

            // 목차 버튼
            ivInsideDiaryIndex.setOnClickListener {
                diaryBookInfo?.let {
                    Intent(requireContext(), IndexDiaryActivity::class.java).apply {
                        putExtra(IndexDiaryActivity.DIARY_BOOk_ID, it.diaryBookId)
                        startActivity(this)
                    }
                }
            }

            clDescription.setOnClickListener {
                //Nothing
            }

            clDescription2.setOnClickListener {
                //Nothing
            }

            // 디스크립션1 x 버튼
            ivDescriptionClose.setOnClickListener {
                insideDiaryViewModel.isShowDescription.value = false
                insideDiaryViewModel.isShowDescription2.value = true
                FromUApplication.prefManager.editor.putBoolean(PrefManager.WHETHER_SHOW_DIARY_DESCRIPTION, false).apply()
            }

            // 디스크립션2 x 버튼
            ivDescription2Close.setOnClickListener {
                insideDiaryViewModel.isShowDescription2.value = false
            }
        }
    }

    private fun initObserve() {
        insideDiaryViewModel.apply {
            allDiariesRes.observe(viewLifecycleOwner, EventObserver { resource ->
                handleResource(resource, true, listener = object : ResourceSuccessListener<AllDiariesRes> {
                    override fun onSuccess(res: AllDiariesRes) {
                        handleAllDiariesRes(res)
                    }
                })
            })

            changeFirstPageImgResult.observe(viewLifecycleOwner) { resources ->
                handleResource(resources, true, object : ResourceSuccessListener<ChangeFirstPageImgRes> {
                    override fun onSuccess(res: ChangeFirstPageImgRes) {
                        handleChangeFirstPageImgRes(res)
                    }
                })
            }
        }

        // backStack
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(AddInsideDiaryFragment.UPLOADED_DIARY_ID)
            ?.observe(viewLifecycleOwner) { uploadedDiaryId ->
                addDiaryToDiariesListAndGoLastPage(uploadedDiaryId)
            }
    }

    private fun callApi() {
        diaryBookInfo?.let {
            insideDiaryViewModel.getAllDiaries(it.diaryBookId)
        } ?: let {
            Utils.showNetworkErrorSnackBar(binding.root)
        }
    }


    /**
     * 일기 작성 완료 후 마지막 페이지로 이동
     */
    private fun addDiaryToDiariesListAndGoLastPage(diaryId: Int) {
        insideDiaryList.add(InsideDiaryModel.Diaries(DetailDiaryResult(diaryId = diaryId)))
        insideDiaryVpAdapter.submitList(insideDiaryList.toMutableList())
        insideDiaryViewModel.currentDiaryPosition.value = insideDiaryList.size - 1
        insideDiaryViewModel.maxLengthOfDiaries.value = insideDiaryList.size - 1
        binding.vpInsideDiary.currentItem = insideDiaryList.size - 1
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
                    selectedCropImgFilePath = it.getStringExtra(ImgCropActivity.CROP_IMG_FILE_PATH)
                    insideDiaryViewModel.changeFirstPageImg(selectedCropImgFilePath)
                }
            } else {
                Utils.showCustomSnackBar(binding.root, getString(R.string.load_img_failure_msg))
            }
        }
    }

    /**
     * All Diaries res 핸들링
     * @param res
     */
    private fun handleAllDiariesRes(res: AllDiariesRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                insideDiaryViewModel.maxLengthOfDiaries.value = res.result.size
                insideDiaryList.addAll(res.result.map { InsideDiaryModel.Diaries(DetailDiaryResult(diaryId = it)) })
                insideDiaryVpAdapter.submitList(insideDiaryList)
            }
        }
    }

    /**
     * 첫 장 대표 이미지 변경 핸들링
     *
     * @param res
     */
    private fun handleChangeFirstPageImgRes(res: ChangeFirstPageImgRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                insideDiaryViewModel.diaryFirstPageFilePath.value = selectedCropImgFilePath
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
            }
        }
    }
}