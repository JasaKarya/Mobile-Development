package com.jasakarya.ui.auth.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.databinding.ActivitySplashBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.auth.category.CategoryActivity
import com.jasakarya.ui.auth.login.LoginActivity
import com.jasakarya.ui.home.HomeActivity
import com.jasakarya.ui.home.HomeViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var _splashBinding: ActivitySplashBinding? = null
    private val splashBinding get() = _splashBinding!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var factory: ViewModelFactory
    private val viewModel: HomeViewModel by viewModels {factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        factory = ViewModelFactory.getInstance(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseAuth.currentUser != null) {
                val userEmail = firebaseAuth.currentUser?.email.toString()
                viewModel.checkIfUserPreferred(userEmail)
                viewModel.userHasPreferences.observe(this) { userHasPreferences ->
                    if (!userHasPreferences) {
                        val intent = Intent(this, CategoryActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                    else{
                        val intent = Intent(this, HomeActivity::class.java)
                        finish()
                        startActivity(intent)

                    }
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, delayMillis)
    }

    private companion object {
        const val delayMillis = 3000L
    }
}