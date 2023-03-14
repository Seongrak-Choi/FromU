package com.fromu.fromu.ui.main.diary.inside.index

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.databinding.ItemIndexMonthInsideDiaryBinding

class IndexMonthAdapter(private val listener: IndexMonthAdapterListener) : ListAdapter<String, IndexMonthAdapter.ViewHolder>(IndexDiffCallback()) {


    interface IndexMonthAdapterListener {
        fun onClick(month: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemIndexMonthInsideDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: ItemIndexMonthInsideDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                month = "${item.substring(0, 4)} ${item.substring(4, 6)}월"

                root.setOnClickListener {
                    listener.onClick(item)
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