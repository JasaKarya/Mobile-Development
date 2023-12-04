package com.jasakarya.ui.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.jasakarya.R
import com.jasakarya.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var isShowPass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tfLayoutPassword.setEndIconOnClickListener {
            isShowPass = !isShowPass
            showPass(isShowPass)
        }

        binding.tfEditPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 8) {
                binding.tfLayoutPassword.error = "Password harus lebih dari 8 karakter"
            } else if (text.length > 8) {
                binding.tfLayoutPassword.error = null
            }
        }
    }

    private fun showPass(isShow: Boolean) {
        val textInput = binding.tfLayoutPassword
        val editText = binding.tfEditPassword
        if (isShow) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            textInput.endIconDrawable = ContextCompat.getDrawable(
                textInput.context,
                R.drawable.visible
            )
        } else {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            textInput.endIconDrawable = ContextCompat.getDrawable(
                textInput.context,
                R.drawable.invisible_1
            )
        }

    }
}