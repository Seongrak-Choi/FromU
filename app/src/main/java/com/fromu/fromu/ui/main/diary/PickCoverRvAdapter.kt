package com.fromu.fromu.ui.main.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fromu.fromu.data.dto.DiaryCoverDto
import com.fromu.fromu.databinding.ItemRvPickCoverBinding
import com.fromu.fromu.model.FindDiaryCover

class PickCoverRvAdapter(private val listener: PickCoverRvListener) : ListAdapter<DiaryCoverDto, PickCoverRvAdapter.ViewHolder>(DiaryCoverDiffCallback()) {

    // 선택한 아이템의 포지션을 저장할 변수
    private var selectItemPosition = -1

    interface PickCoverRvListener {
        fun onClick(diaryCoverDto: DiaryCoverDto)
    }

    init {
        submitList(FindDiaryCover.getDiaryCoverList())
    }

    inner class ViewHolder(val binding: ItemRvPickCoverBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diaryCoverDto: DiaryCoverDto) {
            binding.apply {
                ivPickCover.setImageResource(diaryCoverDto.coverDrawableNoShadow)
                vItemPickCoverChecked.visibility = if (adapterPosition == selectItemPosition) View.VISIBLE else View.GONE

                clItemPickCoverRoot.setOnClickListener {
                    selectItemPosition = layoutPosition
                    listener.onClick(diaryCoverDto)

                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvPickCoverBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    class DiaryCoverDiffCallback : DiffUtil.ItemCallback<DiaryCoverDto>() {
        override fun areItemsTheSame(oldItem: DiaryCoverDto, newItem: DiaryCoverDto): Boolean {
            return oldItem.coverId == newItem.coverId
        }

        override fun areContentsTheSame(oldItem: DiaryCoverDto, newItem: DiaryCoverDto): Boolean {
            return oldItem == newItem
        }
    }
}