package com.fromu.fromu.ui.main.mailbox.stampbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.databinding.ItemSelectStampBinding
import com.fromu.fromu.model.FindStamp

class PurchaseStampListRvAdapter(private val listener: SelectStampListener) : ListAdapter<Int, PurchaseStampListRvAdapter.ViewHolder>(StampDiffUtil()) {
    interface SelectStampListener {
        fun onSelect(stampId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSelectStampBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSelectStampBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stampId: Int) {
            binding.apply {
                ivSelectStamp.setImageResource(FindStamp.getStampDrawableById(stampId))
                vSelect.visibility = View.GONE

                root.setOnClickListener {
                    listener.onSelect(stampId)

                    notifyDataSetChanged()
                }
            }
        }
    }

    class StampDiffUtil : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

}