package com.jasakarya.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
import com.jasakarya.databinding.ActivityTransactionsBinding
import com.jasakarya.di.ViewModelFactory

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: DetailStatusViewModel by viewModels {factory}
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        val userEmail = firebaseAuth.currentUser?.email.toString()

        viewModel.getTransactions(userEmail)
        viewModel.transactionsList.observe(this) {
            if (it != null) {
                val transactionAdapter = TransactionAdapter( onClick = {
                    val intent = Intent(this, DetailStatusActivity::class.java)
                    intent.putExtra("transaksiId", it.transactionId)
                    startActivity(intent)
                })
                binding.apply {
                    with(rvTransaction) {
                        layoutManager = StaggeredGridLayoutManager(1, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
                        setHasFixedSize(true)
                        this.adapter = transactionAdapter
                    }
                    transactionAdapter.submitList(it)
                }
            }
        }


    }
}