package com.jasakarya.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jasakarya.data.repository.Repository
import com.jasakarya.ui.auth.category.CategoryViewModel
import com.jasakarya.ui.auth.login.LoginViewModel
import com.jasakarya.ui.auth.register.SignUpViewModel
import com.jasakarya.ui.cart.CartViewModel
import com.jasakarya.ui.detail.DetailViewModel
import com.jasakarya.ui.home.HomeViewModel
import com.jasakarya.ui.payment.DetailStatusViewModel
import com.jasakarya.ui.payment.PaymentViewModel
import com.jasakarya.ui.profile.ProfileViewModel

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
                modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                    return DetailViewModel(repository) as T
                }
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    return ProfileViewModel(repository) as T
                }
                modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                    return CartViewModel(repository) as T
                }
                modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                    return CategoryViewModel(repository) as T
                }
                modelClass.isAssignableFrom(PaymentViewModel::class.java) -> {
                    return PaymentViewModel(repository) as T
                }
                modelClass.isAssignableFrom(DetailStatusViewModel::class.java) -> {
                    return DetailStatusViewModel(repository) as T
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