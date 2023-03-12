package com.fromu.fromu.ui.main.diary.inside

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.databinding.ItemIndexDayInsideDiaryBinding
import com.fromu.fromu.databinding.ItemIndexMonthInsideDiaryBinding
import com.fromu.fromu.model.IndexInsideDiaryModel

class IndexInsideDiaryAdapter : ListAdapter<IndexInsideDiaryModel, RecyclerView.ViewHolder>(IndexDiffCallback()) {

    companion object {
        const val TYPE_MONTH = 0
        const val TYPE_DAY = 1
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
            is IndexInsideDiaryAdapter.IndexMonthViewHolder -> {
                holder.bind(getItem(position) as IndexInsideDiaryModel.IndexMonth)
            }
            is IndexInsideDiaryAdapter.IndexDayViewHolder -> {
                holder.bind(getItem(position) as IndexInsideDiaryModel.IndexDay)
            }
        }
    }


    inner class IndexMonthViewHolder(private val binding: ItemIndexMonthInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndexInsideDiaryModel.IndexMonth) {

        }
    }

    inner class IndexDayViewHolder(private val binding: ItemIndexDayInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndexInsideDiaryModel.IndexDay) {

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