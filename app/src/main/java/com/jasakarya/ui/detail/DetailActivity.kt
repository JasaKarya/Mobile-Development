package com.jasakarya.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
import com.jasakarya.data.model.Cart
import com.jasakarya.data.model.Content
//import com.jasakarya.data.model.DummyListTalent
//import com.jasakarya.data.model.OrderPackage
import com.jasakarya.databinding.ActivityDetailBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.cart.CartActivity
import java.util.Calendar

//import com.jasakarya.ui.home.ProfileAdapter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var descriptionText: String
    private lateinit var description: TextView
    private lateinit var ilustartorDescText: String
    private lateinit var ilustartorDesc: TextView
    private var isFavorite = false
    private  var content: Content? = null

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var factory : ViewModelFactory
    private val viewModel: DetailViewModel by viewModels {factory}

    var selectedPackage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        setupButtonPackage()

        val contentId = intent.getIntExtra(EXTRA_CONTENT_ID, 0)
        viewModel.getTalent(contentId)
        viewModel.talent.observe(this) { talent ->
            binding.apply{
                Glide.with(this@DetailActivity)
                    .load(talent?.content?.image_url)
                    .into(ivProfileUrl)
                tvTitle.text = talent?.content?.content_name
                tvDescription.text = talent?.content?.description
                tvRate.text = talent?.content?.rating.toString()
                tvRateNumbers.text = ""

                val packageName1 = talent?.content?.packages?.get(0)?.package_name
                val packageName2 = talent?.content?.packages?.get(1)?.package_name
                val packageName3 = talent?.content?.packages?.get(2)?.package_name

                val packagePrice1 = talent?.content?.packages?.get(0)?.package_price
                val packagePrice2 = talent?.content?.packages?.get(1)?.package_price
                val packagePrice3 = talent?.content?.packages?.get(2)?.package_price

                val textPackage1 = "$packageName1\nRp.$packagePrice1"
                val textPackage2 = "$packageName2\nRp.$packagePrice2"
                val textPackage3 = "$packageName3\nRp.$packagePrice3"

                btnPackage1.text = textPackage1
                btnPackage2.text = textPackage2
                btnPackage3.text = textPackage3

                Glide.with(this@DetailActivity)
                    .load(talent?.image_url)
                    .into(ivTalentPhoto)

                tvIlustratorName.text = talent?.talent_name
                tvIlustratorDesc.text = talent?.talent_brief
                tvRateIlustrator.text = talent?.avg_rating.toString()

                content = talent?.content


            }
        }

        binding.btnOrder.setOnClickListener {
            val yearNow = Calendar.getInstance().get(Calendar.YEAR)
            val monthNow = Calendar.getInstance().get(Calendar.MONTH)
            val dayNow = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val hourNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val minuteNow = Calendar.getInstance().get(Calendar.MINUTE)
            val secondNow = Calendar.getInstance().get(Calendar.SECOND)

            val contentId = content?.content_id
            val userEmail = firebaseAuth.currentUser?.email
            val contentName = content?.content_name
            val contentPackage = content?.packages?.get(selectedPackage)
            val contentPrice = contentPackage?.package_price
            val note = binding.tfEditNote.text.toString()

            val cartId = "$userEmail$contentId-$yearNow$monthNow$dayNow$hourNow$minuteNow$secondNow"

            val cart = Cart(
                cartId = cartId,
                userEmail = userEmail!!,
                contentId = contentId.toString(),
                contentName = contentName!!,
                selectedPackage = contentPackage!!,
                contentPrice = contentPrice!!,
                note = note,
                imgUrl = content?.image_url.toString()
            )

            viewModel.pushCart(cart)
            viewModel.cartPushSuccess.observe(this) { cartPushSuccess ->
                if (cartPushSuccess){
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Order Success", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Order Failed", Toast.LENGTH_SHORT).show()

                }
            }


        }

        descriptionText = getString(R.string.detail_description)
        description = binding.tvDescription
        ilustartorDescText = getString(R.string.detail_description)
        ilustartorDesc = binding.tvIlustratorDesc

//        val listOfBenefit = listOf(
//            "Desain Kaos Ilustrasi Kustom (3 Desain)",
//            "Tiga revisi desain",
//            "Pengiriman format digital high resolution",
//            "Bonus: Sesi konsultasi desain selama 1 jam",
//            "Diskon 10% untuk pembelian desain tambahan"
//        )

//        val profiles = listOf(
//            Profile(R.drawable.profile1),
//            Profile(R.drawable.profile2),
//            Profile(R.drawable.profile3),
//            Profile(R.drawable.profile3),
//            Profile(R.drawable.profile3),
//            Profile(R.drawable.profile3),
//            Profile(R.drawable.profile3),
//            )
//        talent = intent.getParcelableExtra(EXTRA_TALENT)

//        talent.let {
//            if (talent != null){
//                populateDetailTalent(talent!!)
//            }
//
//        }

        binding.apply {
            tvFavorite.setOnClickListener {
                isFavorite = !isFavorite
                isFavorite(isFavorite)
            }
        }

//        binding.apply {
//            tvBulletspanCardview1.text = convertToBulletList(listOfBenefit)
//            tvBulletspanCardview2.text = convertToBulletList(listOfBenefit)
//            tvBulletspanCardview3.text = convertToBulletList(listOfBenefit)
//        }
//        val adapterProfile = ProfileAdapter(onClick = { service ->
//
//        })

//        binding.apply {
//            with(rvStackedProfile) {
//                layoutManager = LinearLayoutManager(
//                    this@DetailActivity,
//                    LinearLayoutManager.HORIZONTAL,
//                    false
//                )
//                setHasFixedSize(true)
//                this.adapter = adapterProfile
//            }
//            adapterProfile.submitList(profiles)
//        }


//        binding.apply {
//            fourthLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
//            cardview5.setOnClickListener {
//                val view = if (cardview2.visibility == View.GONE) View.VISIBLE else View.GONE
//                if (cardview2.visibility == View.GONE) {
//                    ibShowMoreCardview.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            ibShowMoreCardview.context,
//                            R.drawable.ic_to_down
//                        )
//                    )
//
//                } else{
//                    ibShowMoreCardview.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            ibShowMoreCardview.context,
//                            R.drawable.ic_to_right
//                        )
//                    )
//                }
//                cardview2.visibility = view
//                cardview3.visibility = view
//                cardview4.visibility = view
//            }
//        }
        setupShowMore(description, descriptionText, 2, 85)
        setupShowMore(ilustartorDesc, ilustartorDescText, 4, 140)
    }

    private fun isFavorite(isFav: Boolean) {
        val tvFavorite = binding.tvFavorite
        if (isFav) {
            val startDrawable = ContextCompat.getDrawable(
                this,
                R.drawable.ic_loved
            )
            tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(
                startDrawable,
                null,
                null,
                null
            )
        } else {
            val startDrawable = ContextCompat.getDrawable(
                this,
                R.drawable.ic_love
            )
            tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(
                startDrawable,
                null,
                null,
                null
            )
        }
    }

    private fun setupShowMore(
        textView: TextView,
        fullText: String,
        limitLines: Int,
        maxLength: Int
    ) {
        if (fullText.length > maxLength)
            setTruncatedText(textView, fullText, maxLength)

        textView.setOnClickListener {
            toggleText(textView, fullText, limitLines, maxLength)
        }
    }

    private fun toggleText(
        textView: TextView,
        fullText: String,
        limitLines: Int,
        maxLength: Int
    ) {
        if (textView.maxLines == Integer.MAX_VALUE) {
            textView.maxLines = limitLines
            setTruncatedText(textView, fullText, maxLength)
        } else {
            textView.maxLines = Integer.MAX_VALUE
            textView.text = fullText
        }
    }

    private fun setTruncatedText(
        textView: TextView,
        fulltext: String,
        maxLength: Int
    ){
        val endText = " ... Read More"
        val truncatedText = fulltext.substring(0, maxLength)
        val truncatedTextBold = SpannableStringBuilder()
            .append(truncatedText)
            .bold { append(endText) }
        textView.text = truncatedTextBold
    }

    private fun convertToBulletList(
        stringList: List<String>
    ): CharSequence {
        val navy = getColor(R.color.navy)
        val spannableBuilder = SpannableStringBuilder()
        stringList.forEachIndexed { index, text ->
            val line: CharSequence = text + if (index < stringList.size - 1) "\n" else ""
            val spannable: Spannable = SpannableString(line)
            spannable.setSpan(
                BulletSpan(15, navy),
                0,
                spannable.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spannableBuilder.append(spannable)
        }
        return spannableBuilder
    }

//    private fun populateDetailTalent(talent: Talent){
//        binding.apply {
//            val content = talent.content.find { content ->  content.contentId == "11122"}
//
//            content?.let {
//                Glide.with(this@DetailActivity)
//                    .load(content.imageUrl)
//                    .into(ivProfileUrl)
//                tvTitle.text = content.contentName
//                tvDescription.text = content.description
//                tvRate.text = content.rating.toString()
//
//                val orderPackage1 = content.orderPackage.find { orderPackage ->  orderPackage.orderPackageId == 11122231 }
//                val orderPackage2 = content.orderPackage.find { orderPackage ->  orderPackage.orderPackageId == 11122232 }
//                val orderPackage3 = content.orderPackage.find { orderPackage ->  orderPackage.orderPackageId == 11122233 }
//
//                tvCardview1.text = orderPackage1!!.orderPackageName
//                tvCardview2.text = orderPackage2!!.orderPackageName
//                tvCardview3.text = orderPackage3!!.orderPackageName
//            }
//
//
//
//        }
//
//    }

    private fun setupButtonPackage(){
        binding.btnPackage1.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
        binding.btnPackage2.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
        binding.btnPackage3.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)

        binding.btnPackage1.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnPackage2.setTextColor(ContextCompat.getColor(this, R.color.blue))
        binding.btnPackage3.setTextColor(ContextCompat.getColor(this, R.color.blue))

        binding.btnPackage1.setOnClickListener {
            selectedPackage = 0
            binding.btnPackage1.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            binding.btnPackage2.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage3.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage1.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnPackage2.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnPackage3.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
        binding.btnPackage2.setOnClickListener {
            selectedPackage = 1
            binding.btnPackage1.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage2.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            binding.btnPackage3.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage1.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnPackage2.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnPackage3.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
        binding.btnPackage3.setOnClickListener {
            selectedPackage = 2
            binding.btnPackage1.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage2.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage3.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            binding.btnPackage1.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnPackage2.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnPackage3.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    companion object{
        const val EXTRA_CONTENT_ID = "extra_content_id"
    }
}