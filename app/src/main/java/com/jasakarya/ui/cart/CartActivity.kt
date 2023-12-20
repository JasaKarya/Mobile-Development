package com.jasakarya.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.databinding.ActivityCartBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.detail.DetailActivity

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: CartViewModel by viewModels {factory}
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val userEmail = firebaseAuth.currentUser?.email.toString()
        viewModel.getCarts(userEmail)

        viewModel.carts.observe(this) { carts ->
            val cartAdapter = CartAdapter(onClick = { content ->
//                val intent = DetailActivity.newIntent(this, content.content_id)
//                startActivity(intent)
            })
            binding.ibBack.setOnClickListener {
                finish()
            }

            binding.apply {
                with(rvCart) {
                    layoutManager = StaggeredGridLayoutManager(1, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
                    setHasFixedSize(true)
                    this.adapter = cartAdapter
                }
                cartAdapter.submitList(carts)
            }
        }

    }
}