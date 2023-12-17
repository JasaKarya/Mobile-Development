package com.jasakarya.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jasakarya.ui.home.HomeAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.jasakarya.data.model.Talent
import com.jasakarya.databinding.ItemRowServiceBinding

class HomeAdapter (
    private val onClick: (Talent) -> Unit
) : ListAdapter<Talent, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
    }

    class MyViewHolder(
        private val binding: ItemRowServiceBinding,
        private val onClick: (Talent) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(talent: Talent) {
            binding.apply {
                Glide.with(root.context)
                    .load(talent.profileUrl)
                    .into(imgItemPhoto)
                tvItemName.text = talent.talentName
                tvRate.text = talent.avgRating.toString()

                root.setOnClickListener {
                    onClick(talent)
                }
            }
        }

        companion object {

            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Talent>() {
                override fun areItemsTheSame(
                    oldItem: Talent,
                    newItem: Talent
                ): Boolean {
                    return oldItem.talentName == newItem.talentName
                }

                override fun areContentsTheSame(
                    oldItem: Talent,
                    newItem: Talent
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}