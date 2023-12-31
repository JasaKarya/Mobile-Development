package com.jasakarya.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
//import com.jasakarya.data.model.DummyListTalent
import com.jasakarya.databinding.ActivityHomeBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.auth.category.CategoryActivity
import com.jasakarya.ui.auth.login.LoginViewModel
import com.jasakarya.ui.cart.CartActivity
import com.jasakarya.ui.detail.DetailActivity
import com.jasakarya.ui.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: HomeViewModel by viewModels {factory}

    private var listPreference = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userEmail = firebaseAuth.currentUser?.email.toString()

        viewModel.getUser(userEmail)
        viewModel.userLiveData.observe(this) { user ->
            if (user != null){
                listPreference = user.preferredCategories
            }
        }


        binding.chipGroup.check(R.id.chip_semua)

        viewModel.getContents(30)
        viewModel.contents.observe(this) { contents ->
            val adapterHome = HomeAdapter(onClick = { content ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_CONTENT_ID, content.content_id)
                startActivity(intent)
            })

            binding.apply {
                with(rvService) {
                    layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                    setHasFixedSize(true)
                    this.adapter = adapterHome
                }
                adapterHome.submitList(contents)
            }
        }

        binding.ibProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.ibCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.fabSearch.setOnClickListener{
            val searchQuery = binding.searchView.query.toString().trim()
            if(searchQuery.isNotEmpty()){
                viewModel.searchContents(searchQuery, 30)
                binding.chipGroup.check(R.id.chip_explore)
            }

        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_relevant -> {
                    viewModel.getFilteredContents(listPreference, 0.0, 30)
                }

                R.id.chip_populer -> {
                    viewModel.getSortedContents(30)
                }

                R.id.chip_semua -> {
                    viewModel.getContents(30)
                }

                R.id.chip_explore -> {

                }
            }
        }



    }


}