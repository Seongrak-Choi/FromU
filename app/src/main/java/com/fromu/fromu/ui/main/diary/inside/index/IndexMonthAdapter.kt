package com.fromu.fromu.ui.main.diary.inside.index

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.databinding.ItemIndexMonthInsideDiaryBinding

class IndexMonthAdapter(private val listener: IndexMonthAdapterListener) : ListAdapter<String, RecyclerView.ViewHolder>(IndexDiffCallback()) {

    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }

    interface IndexMonthAdapterListener {
        fun onClick(month: String)
        fun onClickToFirstPage()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> {
                HeaderViewHolder(ItemIndexMonthInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ITEM -> {
                ItemViewHolder(ItemIndexMonthInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                throw ClassCastException("Unknown viewType IndexMonthAdapter:$viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position))
            }
            is ItemViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            HEADER
        else
            ITEM
    }


    inner class ItemViewHolder(private val binding: ItemIndexMonthInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                month = "${item.substring(0, 4)} ${item.substring(4, 6)}월"

                root.setOnClickListener {
                    listener.onClick(item)
                }
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemIndexMonthInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                month = item

                root.setOnClickListener {
                    listener.onClickToFirstPage()
                }
            }
        }
    }

    class IndexDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}