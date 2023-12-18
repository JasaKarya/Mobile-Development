package com.jasakarya.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jasakarya.data.repository.Repository
import com.jasakarya.ui.auth.login.LoginViewModel
import com.jasakarya.ui.auth.register.SignUpViewModel
import com.jasakarya.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T{

            when{
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    return LoginViewModel(repository) as T
                }
                modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                    return SignUpViewModel(repository) as T
                }
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    return HomeViewModel(repository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }


    }