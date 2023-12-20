package com.jasakarya.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.jasakarya.R
import com.jasakarya.databinding.ActivityMethodPaymentBinding
import com.jasakarya.di.ViewModelFactory

class MethodPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMethodPaymentBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: PaymentViewModel by viewModels {factory}

    private var selectedPayment = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMethodPaymentBinding.inflate(layoutInflater)
        factory = ViewModelFactory.getInstance(this)

        binding.cvGopay.setOnClickListener {
            selectedPayment = "Gopay"
            binding.cvGopay.setCardBackgroundColor(resources.getColor(R.color.silver))
            binding.cvAtmbersama.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvDana.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvLinkaja.setCardBackgroundColor(resources.getColor(R.color.white))
        }
        binding.cvAtmbersama.setOnClickListener {
            selectedPayment = "ATM Bersama"
            binding.cvGopay.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvAtmbersama.setCardBackgroundColor(resources.getColor(R.color.silver))
            binding.cvDana.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvLinkaja.setCardBackgroundColor(resources.getColor(R.color.white))
        }
        binding.cvDana.setOnClickListener {
            selectedPayment = "Dana"
            binding.cvGopay.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvAtmbersama.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvDana.setCardBackgroundColor(resources.getColor(R.color.silver))
            binding.cvLinkaja.setCardBackgroundColor(resources.getColor(R.color.white))
        }
        binding.cvLinkaja.setOnClickListener {
            selectedPayment = "Linkaja"
            binding.cvGopay.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvAtmbersama.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvDana.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.cvLinkaja.setCardBackgroundColor(resources.getColor(R.color.silver))
        }
    }
}