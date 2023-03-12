package com.fromu.fromu.ui.main.diary.inside.index

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.databinding.ItemIndexDayInsideDiaryBinding
import com.fromu.fromu.databinding.ItemIndexMonthInsideDiaryBinding
import com.fromu.fromu.model.IndexInsideDiaryModel

class IndexInsideDiaryAdapter(private val listener: IndexDiInsidearyAdapterListener) : ListAdapter<IndexInsideDiaryModel, RecyclerView.ViewHolder>(IndexDiffCallback()) {

    companion object {
        const val TYPE_MONTH = 0
        const val TYPE_DAY = 1
    }

    interface IndexDiInsidearyAdapterListener {
        fun onClickMonth(month: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MONTH -> IndexMonthViewHolder(ItemIndexMonthInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_DAY -> IndexDayViewHolder(ItemIndexDayInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw java.lang.ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IndexMonthViewHolder -> {
                holder.bind(getItem(position) as IndexInsideDiaryModel.IndexMonth)
            }
            is IndexDayViewHolder -> {
                holder.bind(getItem(position) as IndexInsideDiaryModel.IndexDay)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is IndexInsideDiaryModel.IndexMonth)
        //첫 번째 원소만 헤더로 나타내기 위함
            TYPE_MONTH
        else
            TYPE_DAY
    }


    inner class IndexMonthViewHolder(private val binding: ItemIndexMonthInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndexInsideDiaryModel.IndexMonth) {
            binding.apply {
                month = "${item.item.substring(0, 4)} ${item.item.substring(4, 6)}월"

                root.setOnClickListener {
                    listener.onClickMonth(item.item)
                }
            }
        }
    }

    inner class IndexDayViewHolder(private val binding: ItemIndexDayInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndexInsideDiaryModel.IndexDay) {
            binding.apply {
                indexByMonthResult = item.item
            }
        }
    }

    class IndexDiffCallback : DiffUtil.ItemCallback<IndexInsideDiaryModel>() {
        override fun areItemsTheSame(oldItem: IndexInsideDiaryModel, newItem: IndexInsideDiaryModel): Boolean {
            return if (oldItem is IndexInsideDiaryModel.IndexDay && newItem is IndexInsideDiaryModel.IndexDay) {
                oldItem.item.diaryId == newItem.item.diaryId
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: IndexInsideDiaryModel, newItem: IndexInsideDiaryModel): Boolean {
            return oldItem == newItem
        }
    }
}