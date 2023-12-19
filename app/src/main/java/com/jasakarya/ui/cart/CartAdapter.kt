package com.jasakarya.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jasakarya.data.model.Cart
import com.jasakarya.data.model.Content
import com.jasakarya.databinding.ItemCartUserBinding
import com.jasakarya.databinding.ItemRowServiceBinding
import com.jasakarya.ui.cart.CartAdapter.MyViewHolder.Companion.DIFF_CALLBACK_CART

class CartAdapter (
    private val onClick: (Cart) -> Unit
) : ListAdapter<Cart, CartAdapter.MyViewHolder>(DIFF_CALLBACK_CART) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCartUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
    }

    class MyViewHolder(
        private val binding: ItemCartUserBinding,
        private val onClick: (Cart) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            binding.apply {
                Glide.with(root.context)
                    .load(cart.imgUrl)
                    .into(ivProduct)
                nameProduct.text = cart.contentName
                tvCategory.text = cart.selectedPackage.package_name
                tvPrice.text = cart.contentPrice.toString()

                root.setOnClickListener {
                    onClick(cart)
//                    val intent = Intent(root.context, DetailActivity::class.java)
//                    intent.putExtra(DetailActivity.EXTRA_CONTENT_ID, content.content_id)
//                    it.context.startActivity(intent)

                }
            }
        }

        companion object {

            val DIFF_CALLBACK_CART = object : DiffUtil.ItemCallback<Cart>() {
                override fun areItemsTheSame(
                    oldItem: Cart,
                    newItem: Cart
                ): Boolean {
                    return oldItem.contentName == newItem.contentName
                }

                override fun areContentsTheSame(
                    oldItem: Cart,
                    newItem: Cart
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}