package com.jasakarya.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
import com.jasakarya.data.model.Transaction
import com.jasakarya.databinding.ActivityDetailStatusBinding
import com.jasakarya.di.ViewModelFactory
import java.util.Calendar

class DetailStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStatusBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: DetailStatusViewModel by viewModels {factory}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val transaksiId = intent.getStringExtra("transaksiId")

        viewModel.getTransaction(transaksiId!!)
        viewModel.transaction.observe(this) {
            if (it != null) {
                binding.apply{
                    Glide.with(this@DetailStatusActivity)
                        .load(it.contentImgUrl)
                        .into(ivProduct)
                    nameProduct.text = it.contentName
                    tvCategory.text = it.selectedPackage
                    tvPrice.text = "Rp. ${it.selectedPrice}"

                    tvIlustratorName.text = it.talentName
                    val second = it.paymentDate.second
                    val minute = it.paymentDate.minute
                    val hour = it.paymentDate.hour
                    val day = it.paymentDate.day
                    val month = it.paymentDate.month
                    val year = it.paymentDate.year
                    val formattedDate = "$day/$month/$year $hour:$minute:$second"
                    tvTanggal.text = formattedDate
                    tvEmail.text = it.useremail
                    tvPaymentMethod.text = it.paymentMethod
                    tvNoteDetail.text = it.note
                    tvIdTransaksi.text = it.transactionId
                    tvHargaBarang.text = "Rp. ${it.selectedPrice}"
                    tvTotalHarga.text = "Rp. ${it.selectedPrice}"


                }
                viewModel.getTalent(it.contentId)
                viewModel.talent.observe(this) {
                    if (it != null) {
                        binding.apply {
                            Glide.with(this@DetailStatusActivity)
                                .load(it.image_url)
                                .into(ivTalentPhoto)
                            tvIlustratorDesc.text = it.talent_brief
                            tvRateIlustrator.text = it.avg_rating.toString()

                        }
                    }
                }

            }
        }



    }
}