package com.jasakarya.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.jasakarya.R
import com.jasakarya.databinding.ActivityPaymentBinding
import com.jasakarya.di.ViewModelFactory

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: PaymentViewModel by viewModels {factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val contentId = intent.getIntExtra("contentId", 0)
        val selectedPackage = intent.getStringExtra("selectedPackage")
        val selectedPrice = intent.getIntExtra("selectedPrice", 0)
        val note = intent.getStringExtra("note")
        val contentName = intent.getStringExtra("contentName")
        val imgUrl = intent.getStringExtra("imgUrl")

        val cartId = intent.getStringExtra("cartId")

        binding.apply{
            Glide.with(this@PaymentActivity)
                .load(imgUrl)
                .into(ivProduct)
            nameProduct.text = contentName
            tvCategory.text = selectedPackage
            tvPrice.text = "Rp. $selectedPrice"
            tvOngoing.text = note
            tvHargaBarang.text = "Rp. $selectedPrice"
            tvTotalHarga.text = "Rp. $selectedPrice"
        }

        viewModel.getTalent(contentId)

        binding.btnPay.setOnClickListener {
            val intent = Intent(this, MethodPaymentActivity::class.java)
            intent.putExtra("contentId", contentId)
            intent.putExtra("selectedPackage", selectedPackage)
            intent.putExtra("selectedPrice", selectedPrice)
            intent.putExtra("note", note)
            intent.putExtra("contentName", contentName)
            intent.putExtra("imgUrl", imgUrl)
            intent.putExtra("cartId", cartId)
            startActivity(intent)
        }


    }
}