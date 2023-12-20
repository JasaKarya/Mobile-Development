package com.jasakarya.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.jasakarya.R
import com.jasakarya.databinding.ActivityLoginBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.auth.category.CategoryActivity
import com.jasakarya.ui.auth.register.SignUpActivity
import com.jasakarya.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels {factory}

    private var isShowPass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        try{
            viewModel.logout()
        }
        catch(e: Exception){
            Toast.makeText(this, "Logout Failed: error ${e}", Toast.LENGTH_SHORT).show()
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener{
            val username = binding.tfEditUsername.text?.trim().toString()
            val password = binding.tfEditPassword.text?.trim().toString()

            try{
                viewModel.login(username, password)
            }
            catch(e: Exception){
                Toast.makeText(this, "Login Failed: error ${e}", Toast.LENGTH_SHORT).show()
            }

            viewModel.userLiveData.observe(this) {
                if (it != null) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    viewModel.checkUserHasPreference(it.email.toString())
                    viewModel.userHasPreference.observe(this) { userHasPreferences ->
                        if (!userHasPreferences) {
                            val intent = Intent(this, CategoryActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }




                }
                else{
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.ibBack.visibility = android.view.View.GONE
        binding.btnLogin.isEnabled = false
        togglePass()
        passValid()
        usernameValid()
        checkValid()


    }

    private fun togglePass(){
        binding.tfLayoutPassword.setEndIconOnClickListener {
            isShowPass = !isShowPass
            showPass(isShowPass)
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
    private fun passValid(){
        binding.tfEditPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 7) {
                binding.tfLayoutPassword.error = "Password harus lebih dari 8 karakter"
            } else if (text.length > 7) {
                binding.tfLayoutPassword.error = null
            }
        }
    }

    private fun usernameValid(){
        binding.tfEditUsername.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 3) {
                binding.tfLayoutUsername.error = "Username tidak boleh kosong!"
            } else if (text.length > 3) {
                binding.tfLayoutUsername.error = null
            }
        }
    }

    private fun checkValid(){
        var usernamevalid = false
        var passwordvalid = false

        binding.tfEditUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                val textUsername = string?.trim().toString()
                val textPassword = binding.tfEditPassword.text?.trim().toString()
                if(textUsername.length>3 && textPassword.length>7){
                    binding.btnLogin.isEnabled = true
                }
                else{
                    binding.btnLogin.isEnabled = false
                }
            }

            override fun afterTextChanged(string: Editable?) {}
        })

        binding.tfEditPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                val textPassword = string?.trim().toString()
                val textUsername = binding.tfEditUsername.text?.trim().toString()
                if(textUsername.length>3 && textPassword.length>7){
                    binding.btnLogin.isEnabled = true

                }
                else{
                    binding.btnLogin.isEnabled = false
                }
            }
            override fun afterTextChanged(string: Editable?) {}
        })

        if(usernamevalid && passwordvalid){
            binding.btnLogin.isEnabled = true
        }
        else{
            binding.btnLogin.isEnabled = false
        }
    }






}