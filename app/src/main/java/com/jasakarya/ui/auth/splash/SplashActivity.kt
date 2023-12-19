package com.jasakarya.ui.auth.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.databinding.ActivitySplashBinding
import com.jasakarya.ui.auth.login.LoginActivity
import com.jasakarya.ui.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var _splashBinding: ActivitySplashBinding? = null
    private val splashBinding get() = _splashBinding!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseAuth.currentUser != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
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