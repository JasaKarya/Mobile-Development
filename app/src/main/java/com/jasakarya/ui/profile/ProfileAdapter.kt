package com.jasakarya.ui.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jasakarya.R
import com.jasakarya.ui.profile.ProfileAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.jasakarya.data.model.Profile
import com.jasakarya.databinding.ItemStackedProfileBinding

class ProfileAdapter(private val onClick: (Profile) -> Unit
) : ListAdapter<Profile, ProfileAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStackedProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val list = getItem(position)
        holder.bind(list)
        val profileImage1 = holder.itemView.findViewById<ImageView>(R.id.profileImage1)
        val profileImage2 = holder.itemView.findViewById<ImageView>(R.id.profileImage2)
        val profileNumber = holder.itemView.findViewById<TextView>(R.id.profileNumber)

        if (position in 3 until 4) {
            profileImage2.visibility = View.GONE
            profileImage1.visibility = View.GONE
            profileNumber.visibility = View.VISIBLE
            profileNumber.text = if (itemCount > 99) "99+" else "+${itemCount - 3}"

        }else if (position in 1 until 3){
            profileNumber.visibility = View.GONE
            profileImage1.visibility = View.GONE
            profileImage2.setImageResource(list.photoId)

        } else if (position == 0) {
            profileNumber.visibility = View.GONE
            profileImage2.visibility = View.GONE
            profileImage1.setImageResource(list.photoId)
        }
    }

    class MyViewHolder(
        private val binding: ItemStackedProfileBinding,
        private val onClick: (Profile) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile) {
            binding.apply {

                root.setOnClickListener {
                    onClick(profile)
                }
            }
        }

        companion object {

            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Profile>() {
                override fun areItemsTheSame(
                    oldItem: Profile,
                    newItem: Profile
                ): Boolean {
                    return oldItem.photoId== newItem.photoId
                }

                override fun areContentsTheSame(
                    oldItem: Profile,
                    newItem: Profile
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}