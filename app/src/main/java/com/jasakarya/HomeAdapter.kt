package com.jasakarya

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jasakarya.HomeAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.jasakarya.databinding.ItemRowServiceBinding

class HomeAdapter (
    private val onClick: (Home) -> Unit
) : ListAdapter<Home, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
        private val onClick: (Home) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(home: Home) {
            binding.apply {
                binding.imgItemPhoto.setImageResource(home.photoUrl)
                binding.tvItemName.text = home.title
                binding.tvRate.text = home.rate.toString()
                binding.tvItemDescription.text = "Mulai dari ${home.price}k"

                root.setOnClickListener {
                    onClick(home)
                }
            }
        }

        companion object {

            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Home>() {
                override fun areItemsTheSame(
                    oldItem: Home,
                    newItem: Home
                ): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(
                    oldItem: Home,
                    newItem: Home
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}