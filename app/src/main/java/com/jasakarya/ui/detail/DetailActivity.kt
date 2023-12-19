package com.jasakarya.ui.detail

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jasakarya.R
import com.jasakarya.data.model.Content
//import com.jasakarya.data.model.DummyListTalent
//import com.jasakarya.data.model.OrderPackage
import com.jasakarya.data.model.Profile
import com.jasakarya.data.model.Talent
import com.jasakarya.databinding.ActivityDetailBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.profile.ProfileAdapter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var descriptionText: String
    private lateinit var description: TextView
    private lateinit var ilustartorDescText: String
    private lateinit var ilustartorDesc: TextView
    private var isFavorite = false
    private  var talent: Content? = null

    private lateinit var factory : ViewModelFactory
    private val viewModel: DetailViewModel by viewModels {factory}

    var package1Selected = true
    var package2Selected = false
    var package3Selected = false

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
        val adapterProfile = ProfileAdapter(onClick = { service ->

        })

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
            package1Selected = true
            package2Selected = false
            package3Selected = false
            binding.btnPackage1.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            binding.btnPackage2.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage3.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage1.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnPackage2.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnPackage3.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
        binding.btnPackage2.setOnClickListener {
            package1Selected = false
            package2Selected = true
            package3Selected = false
            binding.btnPackage1.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage2.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            binding.btnPackage3.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            binding.btnPackage1.setTextColor(ContextCompat.getColor(this, R.color.blue))
            binding.btnPackage2.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnPackage3.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }
        binding.btnPackage3.setOnClickListener {
            package1Selected = false
            package2Selected = false
            package3Selected = true
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