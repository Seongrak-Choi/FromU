package com.fromu.fromu.ui.main.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.data.dto.GetNoticeResult
import com.fromu.fromu.databinding.ItemNotificationBinding

class NotificationRvAdapter : ListAdapter<GetNoticeResult, NotificationRvAdapter.ViewHolder>(NoticeResultDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GetNoticeResult) {
            binding.item = item
        }
    }

    class NoticeResultDiffCallback : DiffUtil.ItemCallback<GetNoticeResult>() {
        override fun areItemsTheSame(oldItem: GetNoticeResult, newItem: GetNoticeResult): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: GetNoticeResult, newItem: GetNoticeResult): Boolean {
            return oldItem == newItem
        }
    }


}