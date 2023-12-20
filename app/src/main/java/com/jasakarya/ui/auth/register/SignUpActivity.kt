package com.jasakarya.ui.auth.register

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.jasakarya.R
import com.jasakarya.data.model.Birthdate
import com.jasakarya.data.model.User
import com.jasakarya.databinding.ActivitySignUpBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.auth.login.LoginActivity
import com.jasakarya.ui.auth.login.LoginViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: SignUpViewModel by viewModels {factory}

    private var isShowPass: Boolean = false
    var date = Birthdate(0,0,0)
    var gender = "Laki Laki"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)


        binding.btnSignUp.isEnabled = false

        binding.tfLayoutPassword.setEndIconOnClickListener {
            isShowPass = !isShowPass
            showPass(isShowPass)
        }

        binding.btnSignUp.setOnClickListener {
            val textUsername = binding.tfEditUsername.text?.trim().toString()
            val textEmail = binding.tfEditEmail.text?.trim().toString()
            val textPassword = binding.tfEditPassword.text?.trim().toString()
            val textFullName = binding.tfEditFullname.text?.trim().toString()
            val textLocation = binding.tfEditLocation.text?.trim().toString()
            val preference = mutableListOf<String>()

            val user = User(textUsername,textEmail,textFullName,gender,date,textLocation, preference)
            try{
                viewModel.register(user,textPassword)
            }
            catch(e: Exception){
                Log.d("SignUpActivity", "onCreate: ${e}")
            }

            viewModel.userLiveData.observe(this) {
                if (it != null) {
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else{
                    Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
                }
            }

        }




        binding.btnDateofbirth.setOnClickListener {
            datePicker()
            checkInput()
        }

        checkValid()
        setupSpinner()




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

    private fun checkInput(){
        val textUsername = binding.tfEditUsername?.text?.trim().toString()
        val textEmail = binding.tfEditEmail?.text?.trim().toString()
        val textPassword = binding.tfEditPassword?.text?.trim().toString()
        val textFullName = binding.tfEditFullname?.text?.trim().toString()
        val textLocation = binding.tfEditLocation?.text?.trim().toString()
        val nullDate = Birthdate(0,0,0)

        if(textUsername.length>3){
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                if(textPassword.length>7){
                    if(textFullName.length>3){
                        if(textLocation.length>3){
                            if(date != nullDate){
                                binding.btnSignUp.isEnabled = true
                                binding.btnSignUp.error = null
                                binding.btnDateofbirth.error = null
                            }
                            else{
                                binding.btnDateofbirth.error = "Tanggal lahir tidak boleh kosong!"
                            }
                            binding.tfLayoutLocation.error = null
                        }
                        else{
                            binding.tfLayoutLocation.error = "Lokasi tidak boleh kosong!"
                        }
                        binding.tfLayoutFullname.error = null
                    }
                    else{
                        binding.tfLayoutFullname.error = "Nama lengkap tidak boleh kosong!"
                    }
                    binding.tfLayoutPassword.error = null
                }
                binding.tfLayoutEmail.error = null
            }
            else{
                binding.tfLayoutEmail.error = "Email tidak valid!"

            }
            binding.tfLayoutUsername.error = null
        }
        else{
            binding.tfLayoutUsername.error = "Username tidak boleh kosong!"
        }


    }

    private fun checkValid(){

        binding.tfEditUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
            checkInput()
            }

            override fun afterTextChanged(string: Editable?) {}
        })

        binding.tfEditPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                checkInput()
            }
            override fun afterTextChanged(string: Editable?) {}
        })

        binding.tfEditEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                checkInput()
            }
            override fun afterTextChanged(string: Editable?) {}
        })

        binding.tfEditFullname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                checkInput()
            }
            override fun afterTextChanged(string: Editable?) {}
        })

        binding.tfEditLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                checkInput()
            }
            override fun afterTextChanged(string: Editable?) {}
        })
    }

    private fun datePicker(){
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                date = Birthdate(selectedDay, selectedMonth, selectedYear)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                binding.btnDateofbirth.text = formattedDate
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun setupSpinner() {

        val enumGender = arrayOf<String?>("Laki Laki", "Perempuan")
        val genderSpinner = binding.spinnerGender
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, enumGender)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender = genderSpinner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                genderSpinner.setSelection(0)
            }

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