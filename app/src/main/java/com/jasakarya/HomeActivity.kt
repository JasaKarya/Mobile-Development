package com.jasakarya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jasakarya.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapterFilter = FilterAdapter(onClick = { title ->
            when (title.text) {
                "Relevan" -> {

                }
                "Populer" -> {

                }
                "Terlaris" -> {

                }
                "Terderkat" -> {

                }
            }
        })
        val adapterHome = HomeAdapter(onClick = { service ->

        })

        val textButtonList = arrayListOf(
            ButtonFilter("Relevan"),
            ButtonFilter("Populer"),
            ButtonFilter("Terlaris"),
            ButtonFilter("Tedekat"),
        )
        binding.apply {
            with(rvFilter) {
                layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                this.adapter = adapterFilter
            }
            adapterFilter.submitList(textButtonList)
        }

        val listHome = listOf(
            Home("Costume Merchandise", 50, 5, R.drawable.custom_merch),
            Home("Desain Poster", 50, 5, R.drawable.desain_poster),
            Home("Template Presentasi", 50, 5, R.drawable.template_presentasi),
        )

        binding.apply {
            with(rvService) {
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                setHasFixedSize(true)
                this.adapter = adapterHome
            }
            adapterHome.submitList(listHome)
        }

    }


}