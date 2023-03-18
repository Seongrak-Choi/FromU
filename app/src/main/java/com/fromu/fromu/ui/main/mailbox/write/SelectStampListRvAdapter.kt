package com.fromu.fromu.ui.main.mailbox.write

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.databinding.ItemSelectStampBinding
import com.fromu.fromu.model.FindStamp

class SelectStampListRvAdapter(private val listener: SelectStampListener) : ListAdapter<Int, SelectStampListRvAdapter.ViewHolder>(StampDiffUtil()) {
    // 선택한 아이템의 포지션을 저장할 변수
    private var selectItemPosition = -1

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
                vSelect.visibility = if (adapterPosition == selectItemPosition) View.VISIBLE else View.GONE

                root.setOnClickListener {
                    selectItemPosition = adapterPosition
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