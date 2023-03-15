package com.fromu.fromu.ui.main.mailbox.maillist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.data.dto.MailListResult
import com.fromu.fromu.databinding.ItemSendMailBinding
import com.fromu.fromu.model.listener.MailListListener

class SendMailListRvAdapter(private val listener: MailListListener) : ListAdapter<MailListResult, SendMailListRvAdapter.ViewHolder>(MailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSendMailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: ItemSendMailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mailListResult: MailListResult) {
            binding.apply {
                item = mailListResult

                root.setOnClickListener {
                    listener.onSelect(mailListResult)
                }
            }
        }
    }


    class MailDiffCallback() : DiffUtil.ItemCallback<MailListResult>() {
        override fun areItemsTheSame(oldItem: MailListResult, newItem: MailListResult): Boolean {
            return oldItem.letterId == newItem.letterId
        }

        override fun areContentsTheSame(oldItem: MailListResult, newItem: MailListResult): Boolean {
            return oldItem == newItem
        }
    }


}