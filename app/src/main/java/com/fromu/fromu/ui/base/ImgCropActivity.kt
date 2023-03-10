package com.fromu.fromu.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.canhub.cropper.CropImageOptions
import com.fromu.fromu.FromUApplication
import com.fromu.fromu.databinding.ActivityImgCropBinding
import com.fromu.fromu.utils.ImageUtils
import com.fromu.fromu.utils.UiUtils

class ImgCropActivity : BaseActivity<ActivityImgCropBinding>(ActivityImgCropBinding::inflate) {
    companion object {
        const val CROP_RESULT_CODE = 100
        const val GALLERY_PICK_IMG = "galleryPickImg"
        const val CROP_IMG_FILE_PATH = "cropImgFilePath"
    }

    private var pickImgUri: Uri? = null //사용자가 선택한 사진의 uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        pickImgUri = intent.getStringExtra(GALLERY_PICK_IMG)?.toUri()
    }

    private fun initView() {
        UiUtils.setFullScreenWithStatusBar(this)

        binding.apply {
            root.setPadding(0, FromUApplication.statusHeight, 0, 0)
            cropVImg.setImageUriAsync(pickImgUri)
            cropVImg.setImageCropOptions(CropImageOptions(fixAspectRatio = true, aspectRatioX = 1, aspectRatioY = 1))
        }

        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            tvImgCropDone.setOnClickListener {
                val imgFile = ImageUtils.bitmapToFile(this@ImgCropActivity, cropVImg.getCroppedImage(1024, 1024))

                Intent().apply {
                    putExtra(CROP_IMG_FILE_PATH, imgFile.absolutePath)
                    setResult(CROP_RESULT_CODE, this)
                    finish()
                }
            }
        }
    }
}