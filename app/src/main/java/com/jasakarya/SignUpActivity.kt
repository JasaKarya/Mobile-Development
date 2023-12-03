package com.jasakarya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.jasakarya.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private var isShowPass: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tfLayoutPassword.setEndIconOnClickListener {
            isShowPass = !isShowPass
            showPass(isShowPass)
        }
        binding.tfEditPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 8) {
                binding.tfLayoutPassword.error = "Password harus lebih dari 8 karakter"

            } else if (text.length > 9) {
                binding.tfLayoutPassword.error = null
            }
        }
        binding.tfEditEmail.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                if (Patterns.EMAIL_ADDRESS.matcher(text).matches() && text.isNotEmpty()) {
                    binding.tfLayoutEmail.error = null

                } else
                    binding.tfLayoutEmail.error = "Format Email tidak sesuai"
            }

        }


//        binding.btnSignUp.setOnClickListener {
//            val name = binding.tfEditUsername.text.toString()
//            val email = binding.tfEditEmail.text.toString()
//            val pass = binding.tfEditPassword.text.toString()
//
//            if (binding.tfLayoutEmail.error.isNullOrEmpty() && binding.tfLayoutPassword.error.isNullOrEmpty()) {
//                viewModel.registerUser(name, email, pass)
//                viewModel.uiState.observe(this) { result ->
//                    if (result != null) {
//                        when (result) {
//                            is RegisUiState.Success -> {
//                                AlertDialog.Builder(this).apply {
//                                    setTitle("Yeah!")
//                                    setMessage("${result.data.message}\nAkun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
//                                    setPositiveButton("Lanjut") { _, _ ->
//                                        finish()
//                                    }
//                                    create()
//                                    show()
//                                }
//
//                            }
//                            is RegisUiState.Error -> {
//                                showLoading(false)
//                                AlertDialog.Builder(this).apply {
//                                    setTitle("Oops!")
//                                    setMessage("${result.message}\nPastikan Email dan Pasword yang anda masukan sesuai")
//                                    setPositiveButton("Lanjut") { _, _ ->
//                                    }
//                                    create()
//                                    show()
//                                }
//                            }
//                            is RegisUiState.Loading -> {
//                                showLoading(true)
//
//                            }
//                        }
//                    }
//                }
//
//            } else {
//                AlertDialog.Builder(this).apply {
//                    setTitle("Oops!")
//                    setMessage("Pastikan Email dan Pasword yang anda masukan sesuai")
//                    setPositiveButton("Lanjut") { _, _ ->
//                    }
//                    create()
//                    show()
//                }
//            }
//        }
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
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            textInput.endIconDrawable = ContextCompat.getDrawable(
                textInput.context,
                R.drawable.invisible_1
            )
        }
    }

//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }
}