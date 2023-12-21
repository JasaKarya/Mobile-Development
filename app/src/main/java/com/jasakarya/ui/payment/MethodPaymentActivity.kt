package com.jasakarya.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
import com.jasakarya.data.model.Date
import com.jasakarya.data.model.Transaction
import com.jasakarya.databinding.ActivityMethodPaymentBinding
import com.jasakarya.di.ViewModelFactory
import java.util.Calendar

class MethodPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMethodPaymentBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: PaymentViewModel by viewModels {factory}

    private var selectedPayment = "Gopay"

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var talentId = ""
    private var talentImgUrl = ""
    private var talentName = ""
    private var talentBrief = ""
    private var talentRating = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMethodPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        setupCardClick()

        val contentId = intent.getIntExtra("contentId", 0)
        val selectedPackage = intent.getStringExtra("selectedPackage")
        val selectedPrice = intent.getIntExtra("selectedPrice", 0)
        val note = intent.getStringExtra("note")
        val contentName = intent.getStringExtra("contentName")
        val imgUrl = intent.getStringExtra("imgUrl")

        val cartId = intent.getStringExtra("cartId")

        val userEmail = firebaseAuth.currentUser?.email.toString()


        viewModel.getTalent(contentId)
        viewModel.fetchedTalent.observe(this) {
            if (it != null) {
                talentId = it.talent_id
                talentName = it.talent_name
                talentBrief = it.talent_brief
                talentImgUrl = it.image_url
                talentRating = it.avg_rating.toString()
                binding.btnPay.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val secondNow = calendar.get(Calendar.SECOND)
                    val minuteNow = calendar.get(Calendar.MINUTE)
                    val hourNow = calendar.get(Calendar.HOUR_OF_DAY)
                    val dayNow = calendar.get(Calendar.DAY_OF_MONTH)
                    val monthNow = calendar.get(Calendar.MONTH) + 1
                    val yearNow = calendar.get(Calendar.YEAR)

                    val transaksiId = "$contentId-$dayNow$monthNow$yearNow$hourNow$minuteNow$secondNow"

                    val date = Date(
                        second = secondNow,
                        minute = minuteNow,
                        hour = hourNow,
                        day = dayNow,
                        month = monthNow,
                        year = yearNow
                    )
                    val transaksi = Transaction(
                        transaksiId,
                        userEmail,
                        talentId,
                        talentName,
                        contentId,
                        contentName!!,
                        imgUrl!!,
                        selectedPackage!!,
                        selectedPrice,
                        note!!,
                        selectedPayment,
                        date

                    )

                    viewModel.addTransaction(transaksi)
                    viewModel.addTransactionSuccess.observe(this) {
                        if(it) {
                            viewModel.deleteCart(cartId!!)
                            Toast.makeText(this, "Transaksi berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, DetailStatusActivity::class.java)
                            intent.putExtra("transaksiId", transaksiId)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Transaksi gagal", Toast.LENGTH_SHORT).show()
                        }
                    }



                }
            }
        }

        binding.btnPay.setOnClickListener {
            val intent = Intent(this, DetailStatusActivity::class.java)
            intent.putExtra("contentId", contentId)
            intent.putExtra("selectedPackage", selectedPackage)
            intent.putExtra("selectedPrice", selectedPrice)
            intent.putExtra("note", note)
            intent.putExtra("contentName", contentName)
            intent.putExtra("imgUrl", imgUrl)
            intent.putExtra("selectedPayment", selectedPayment)
            startActivity(intent)
            finish()
        }


    }

    private fun setupCardClick(){
        val color = ContextCompat.getColor(this, R.color.gold)
        binding.cvGopay.setCardBackgroundColor(color)

        binding.cvGopay.setOnClickListener {
            selectedPayment = "Gopay"
            val color = ContextCompat.getColor(this, R.color.gold)
            val default = ContextCompat.getColor(this, R.color.white)
            binding.cvGopay.setCardBackgroundColor(color)
            binding.cvDana.setCardBackgroundColor(default)
            binding.cvAtmbersama.setCardBackgroundColor(default)
            binding.cvLinkaja.setCardBackgroundColor(default)
        }
        binding.cvDana.setOnClickListener {
            selectedPayment = "Dana"
            val color = ContextCompat.getColor(this, R.color.gold)
            val default = ContextCompat.getColor(this, R.color.white)
            binding.cvGopay.setCardBackgroundColor(default)
            binding.cvDana.setCardBackgroundColor(color)
            binding.cvAtmbersama.setCardBackgroundColor(default)
            binding.cvLinkaja.setCardBackgroundColor(default)
        }
        binding.cvAtmbersama.setOnClickListener {
            selectedPayment = "ATM Bersama"
            val color = ContextCompat.getColor(this, R.color.gold)
            val default = ContextCompat.getColor(this, R.color.white)
            binding.cvGopay.setCardBackgroundColor(default)
            binding.cvDana.setCardBackgroundColor(default)
            binding.cvAtmbersama.setCardBackgroundColor(color)
            binding.cvLinkaja.setCardBackgroundColor(default)
        }
        binding.cvLinkaja.setOnClickListener {
            selectedPayment = "LinkAja"
            val color = ContextCompat.getColor(this, R.color.gold)
            val default = ContextCompat.getColor(this, R.color.white)
            binding.cvGopay.setCardBackgroundColor(default)
            binding.cvDana.setCardBackgroundColor(default)
            binding.cvAtmbersama.setCardBackgroundColor(default)
            binding.cvLinkaja.setCardBackgroundColor(color)
        }
    }


}