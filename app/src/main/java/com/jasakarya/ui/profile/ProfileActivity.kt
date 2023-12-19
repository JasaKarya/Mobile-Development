package com.jasakarya.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
import com.jasakarya.databinding.ActivityProfileBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.auth.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: ProfileViewModel by viewModels {factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        factory = ViewModelFactory.getInstance(this)
        val email = firebaseAuth.currentUser?.email.toString()

        viewModel.getUser(email)
        viewModel.user.observe(this) { user ->
            binding.apply {
                tvUsername.text = user?.username
                tvEmail.text = user?.email
                tvFullname.text = user?.full_name
                val birthYear = user?.date_of_birth?.year.toString()
                val birthMonth = user?.date_of_birth?.month.toString()
                val birthDay = user?.date_of_birth?.day.toString()
                val birthDateString = "$birthDay-$birthMonth-$birthYear"
                tvTanggallahir.text = birthDateString
                tvJk.text = user?.gender
                tvAddress.text = user?.location
            }
        }

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}