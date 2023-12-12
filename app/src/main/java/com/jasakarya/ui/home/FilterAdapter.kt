package com.jasakarya.ui.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.jasakarya.R
import com.jasakarya.ui.home.FilterAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.jasakarya.databinding.ItemRowFilterBinding

class FilterAdapter(
    private val onClick: (ButtonFilter) -> Unit
) : ListAdapter<ButtonFilter, FilterAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var selectedItemPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val list = getItem(position)
        holder.bind(list)
        holder.itemView.setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()
        }
        if (selectedItemPosition == position) {
            holder.itemView.findViewById<CardView>(R.id.cv_filter).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#279EFF"))
            holder.itemView.findViewById<TextView>(R.id.tv_name_button).setTextColor(Color.parseColor("#FFFFFFFF"))
        } else {
            holder.itemView.findViewById<CardView>(R.id.cv_filter).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
            holder.itemView.findViewById<TextView>(R.id.tv_name_button).setTextColor(Color.parseColor("#279EFF"))
        }
    }

    class MyViewHolder(
        private val binding: ItemRowFilterBinding,
        private val onClick: (ButtonFilter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: ButtonFilter) {
            binding.apply {
                binding.tvNameButton.text = name.text

                root.setOnClickListener {
                    onClick(name)
                }
            }
        }

        companion object {

            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ButtonFilter>() {
                override fun areItemsTheSame(
                    oldItem: ButtonFilter,
                    newItem: ButtonFilter
                ): Boolean {
                    return oldItem.text== newItem.text
                }

                override fun areContentsTheSame(
                    oldItem: ButtonFilter,
                    newItem: ButtonFilter
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}