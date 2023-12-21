package com.jasakarya.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jasakarya.data.model.Cart
import com.jasakarya.data.model.Transaction
import com.jasakarya.databinding.ItemTransactionBinding
import com.jasakarya.ui.payment.TransactionAdapter.MyViewHolder.Companion.DIFF_CALLBACK_TRANSACTION

class TransactionAdapter (
    private val onClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.MyViewHolder>(DIFF_CALLBACK_TRANSACTION) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
    }

    class MyViewHolder(
        private val binding: ItemTransactionBinding,
        private val onClick: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.apply {
                nameProduct.text = transaction.contentName
                tvIdTransaksi.text = transaction.transactionId
                val second = transaction.paymentDate.second.toString()
                val minute = transaction.paymentDate.minute.toString()
                val hour = transaction.paymentDate.hour.toString()
                val day = transaction.paymentDate.day.toString()
                val month = transaction.paymentDate.month.toString()
                val year = transaction.paymentDate.year.toString()
                val formattedDate = "$day/$month/$year $hour:$minute:$second"
                tvTanggal.text = formattedDate
                tvPrice.text = transaction.selectedPrice.toString()


                root.setOnClickListener {
                    onClick(transaction)
                }

            }
        }

        companion object {

            val DIFF_CALLBACK_TRANSACTION = object : DiffUtil.ItemCallback<Transaction>() {
                override fun areItemsTheSame(
                    oldItem: Transaction,
                    newItem: Transaction
                ): Boolean {
                    return oldItem.contentName == newItem.contentName
                }

                override fun areContentsTheSame(
                    oldItem: Transaction,
                    newItem: Transaction
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}