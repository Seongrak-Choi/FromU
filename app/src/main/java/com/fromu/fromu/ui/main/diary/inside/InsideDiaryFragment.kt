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
import com.fromu.fromu.data.dto.FirstPageResult
import com.fromu.fromu.data.dto.IndexDiaryInfo
import com.fromu.fromu.data.remote.network.response.AllDiariesRes
import com.fromu.fromu.data.remote.network.response.ChangeFirstPageImgRes
import com.fromu.fromu.data.remote.network.response.FirstPageRes
import com.fromu.fromu.databinding.FragmentInsideDiaryBinding
import com.fromu.fromu.model.InsideDiaryModel
import com.fromu.fromu.model.listener.ResourceSuccessListener
import com.fromu.fromu.ui.base.BaseFragment
import com.fromu.fromu.ui.base.ImgCropActivity
import com.fromu.fromu.ui.main.diary.inside.index.IndexByMonthFragment
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

    private lateinit var firstPageResult: FirstPageResult

    // 디테일 라이프로그 콜백 처리
    val detailLifelogLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == IndexByMonthFragment.INDEX_BY_MONTH_CODE) {
                // 디테일 라이프로그에서 콜백으로 어떤 동작을 할 것인지 전달 받음
                val indexDiaryInfo = result.data?.customGetSerializable<IndexDiaryInfo>(IndexByMonthFragment.INDEX_DIARY_INFO)
                goToSelectPositionPageByDiaryId(indexDiaryInfo)
            }
        }

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
                Intent(requireContext(), IndexDiaryActivity::class.java).apply {
                    putExtra(IndexDiaryActivity.DIARY_BOOk_ID, firstPageResult.diaryBookId)
                    detailLifelogLauncher.launch(this)
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

            firstPageResult.observe(viewLifecycleOwner, EventObserver { resource ->
                handleResource(resource, listener = object : ResourceSuccessListener<FirstPageRes> {
                    override fun onSuccess(res: FirstPageRes) {
                        handleFirstPageResult(res)
                    }
                })
            })

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
        insideDiaryViewModel.getFirstPage()
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
     * 인덱스에서 선택한 diaryId로 페이지 이동
     *
     * @param indexDiaryInfo
     */
    private fun goToSelectPositionPageByDiaryId(indexDiaryInfo: IndexDiaryInfo?) {
        val currentList = insideDiaryVpAdapter.currentList
        var findIndex: Int = 0


        indexDiaryInfo?.let { info ->
            for (i in currentList.indices) {
                if (currentList[i] is InsideDiaryModel.Diaries) {
                    if ((currentList[i] as InsideDiaryModel.Diaries).item.diaryId == info.diaryId)
                        findIndex = i
                }
            }
        }

        binding.vpInsideDiary.currentItem = findIndex
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
     * 첫 장 조회
     *
     * @param res
     */
    private fun handleFirstPageResult(res: FirstPageRes) {
        when (res.code) {
            Const.SUCCESS_CODE -> {
                firstPageResult = res.result
                insideDiaryViewModel.getAllDiaries(firstPageResult.diaryBookId)
                insideDiaryList.add(InsideDiaryModel.Header(firstPageResult))
                insideDiaryViewModel.diaryFirstPageFilePath.value = firstPageResult.imageUrl
            }
            else -> {
                Utils.showNetworkErrorSnackBar(binding.root)
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