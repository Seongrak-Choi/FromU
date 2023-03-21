package com.fromu.fromu.ui.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.data.dto.DetailScheduleResult
import com.fromu.fromu.databinding.ItemDetailScheduleAddBinding
import com.fromu.fromu.databinding.ItemDetailScheduleBinding

class DetailScheduleRvAdapter(private val listener: DetailScheduleRvListener) : ListAdapter<DetailScheduleResult, RecyclerView.ViewHolder>(DetailScheduleDiffCallback()) {

    companion object {
        const val ITEM_TYPE = 0
        const val ADD_TYPE = 1
    }

    interface DetailScheduleRvListener {
        fun onClickDelete(scheduleId: Int, position: Int)
        fun onClickModify(content: String, scheduleId: Int)
        fun onAddSchedule()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE -> {
                ItemViewHolder(ItemDetailScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ADD_TYPE -> {
                AddViewHolder(ItemDetailScheduleAddBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> throw ClassCastException("Unknown viewType DetailScheduleRvAdapter:$viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bind(getItem(position))
            }
            is AddViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size - 1)
            ADD_TYPE
        else
            ITEM_TYPE
    }

    inner class ItemViewHolder(private val binding: ItemDetailScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailScheduleResult) {
            binding.apply {
                detailSchedule = item

                ivDetailScheduleModify.setOnClickListener {
                    listener.onClickModify(item.content, item.scheduleId)
                }

                ivDetailScheduleDelete.setOnClickListener {
                    listener.onClickDelete(item.scheduleId, adapterPosition)
                }
            }
        }
    }

    inner class AddViewHolder(private val binding: ItemDetailScheduleAddBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailScheduleResult) {
            binding.root.setOnClickListener {
                listener.onAddSchedule()
            }
        }
    }

    class DetailScheduleDiffCallback : DiffUtil.ItemCallback<DetailScheduleResult>() {
        override fun areItemsTheSame(oldItem: DetailScheduleResult, newItem: DetailScheduleResult): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(oldItem: DetailScheduleResult, newItem: DetailScheduleResult): Boolean {
            return oldItem == newItem
        }
    }
}