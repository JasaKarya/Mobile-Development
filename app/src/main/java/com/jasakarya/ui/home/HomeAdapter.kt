package com.jasakarya.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.jasakarya.ui.home.HomeAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.jasakarya.data.model.Content
import com.jasakarya.databinding.ItemRowServiceBinding
import com.jasakarya.ui.home.HomeAdapter.MyViewHolder.Companion.DIFF_CALLBACK

class HomeAdapter (
    private val onClick: (Content) -> Unit
) : ListAdapter<Content, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
        private val onClick: (Content) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Content) {
            binding.apply {
                Glide.with(root.context)
                    .load(content.image_url)
                    .into(imgItemPhoto)
                tvItemName.text = content.content_name
                tvRate.text = content.rating.toString()

                root.setOnClickListener {
                    onClick(content)
                }
            }
        }

        companion object {

            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Content>() {
                override fun areItemsTheSame(
                    oldItem: Content,
                    newItem: Content
                ): Boolean {
                    return oldItem.content_name == newItem.content_name
                }

                override fun areContentsTheSame(
                    oldItem: Content,
                    newItem: Content
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}