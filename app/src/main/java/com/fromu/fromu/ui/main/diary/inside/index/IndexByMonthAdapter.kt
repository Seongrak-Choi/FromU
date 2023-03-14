package com.fromu.fromu.ui.main.diary.inside.index

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.data.dto.IndexDiaryInfo
import com.fromu.fromu.databinding.ItemIndexDayInsideDiaryBinding

class IndexByMonthAdapter(private val listener: IndexByMonthAdapterAdapterListener) : ListAdapter<IndexDiaryInfo, IndexByMonthAdapter.ViewHolder>(IndexDiffCallback()) {

    interface IndexByMonthAdapterAdapterListener {
        fun onClick(item: IndexDiaryInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemIndexDayInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: ItemIndexDayInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndexDiaryInfo) {
            binding.apply {
                indexByMonthResult = item

                root.setOnClickListener {
                    listener.onClick(item)
                }
            }
        }
    }

    class IndexDiffCallback : DiffUtil.ItemCallback<IndexDiaryInfo>() {
        override fun areItemsTheSame(oldItem: IndexDiaryInfo, newItem: IndexDiaryInfo): Boolean {
            return oldItem.diaryId == newItem.diaryId

        }

        override fun areContentsTheSame(oldItem: IndexDiaryInfo, newItem: IndexDiaryInfo): Boolean {
            return oldItem == newItem
        }
    }
}