package com.fromu.fromu.ui.main.diary.inside

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DiaryBook
import com.fromu.fromu.data.remote.network.response.DetailDiaryRes
import com.fromu.fromu.databinding.ItemHeadrInsideDiaryBinding
import com.fromu.fromu.databinding.ItemInsideDiaryBinding
import com.fromu.fromu.model.InsideDiaryModel
import com.fromu.fromu.model.listener.DetailDiaryListener
import com.fromu.fromu.ui.dialog.DialogPopupOneBtn
import com.fromu.fromu.utils.Const
import com.fromu.fromu.utils.Utils
import com.fromu.fromu.viewmodels.InsideDiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsideDiaryVpAdapter(
    private val childFragmentManager: FragmentManager,
    private val insideDiaryViewModel: InsideDiaryViewModel,
    private val galleryLauncher: ActivityResultLauncher<Intent>
) : ListAdapter<InsideDiaryModel, RecyclerView.ViewHolder>(InsideDiaryDiffCallback()) {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ItemHeadrInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_ITEM -> ItemViewHolder(ItemInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw java.lang.ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position) as InsideDiaryModel.Header)
            }
            is ItemViewHolder -> {
                holder.bind(getItem(position) as InsideDiaryModel.Diaries)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
        //첫 번째 원소만 헤더로 나타내기 위함
            TYPE_HEADER
        else
            TYPE_ITEM
    }


    inner class HeaderViewHolder(private val binding: ItemHeadrInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val mContext = binding.root.context

        fun bind(item: InsideDiaryModel.Header) {
            binding.apply {
                diaryBook = item.item

                vImgArea.setOnClickListener {
                    if (item.item.imageUrl == null) {
                        Utils.goGalleryWithSinglePicture(galleryLauncher)
                    } else {
                        DialogPopupOneBtn(mContext.getString(R.string.change_first_page_img), mContext.getString(R.string.change)) {
                            Utils.goGalleryWithSinglePicture(galleryLauncher)
                        }.show(childFragmentManager, DialogPopupOneBtn.TAG)
                    }
                }

                CoroutineScope(Dispatchers.Main).launch {
                    insideDiaryViewModel.diaryFirstPageFilePath.collect {
                        item.item.imageUrl = it
                        diaryBook = DiaryBook(item.item.coverNum, item.item.diaryBookId, it, item.item.name, item.item.writeFlag)
                    }
                }
            }
        }
    }


    inner class ItemViewHolder(private val binding: ItemInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diaryInfo: InsideDiaryModel.Diaries) {
            binding.detailDiary = diaryInfo.item

            insideDiaryViewModel.getDetailDiaries(diaryInfo.item.diaryId, object : DetailDiaryListener {
                override fun onSuccess(detailDiaryRes: DetailDiaryRes) {
                    when (detailDiaryRes.code) {
                        Const.SUCCESS_CODE -> {
                            binding.detailDiary = detailDiaryRes.result
                        }
                        else -> {
                            Utils.showNetworkErrorSnackBar(binding.root)
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    Utils.showNetworkErrorSnackBar(binding.root)
                }
            })
        }
    }


    class InsideDiaryDiffCallback : DiffUtil.ItemCallback<InsideDiaryModel>() {
        override fun areItemsTheSame(oldItem: InsideDiaryModel, newItem: InsideDiaryModel): Boolean {
            return if (oldItem is InsideDiaryModel.Diaries && newItem is InsideDiaryModel.Diaries) {
                oldItem.item.diaryId == newItem.item.diaryId
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: InsideDiaryModel, newItem: InsideDiaryModel): Boolean {
            return oldItem == newItem
        }
    }
}