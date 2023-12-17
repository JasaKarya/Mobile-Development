package com.jasakarya.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.model.User
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository): ViewModel(){

    val userLiveData = repository.userLiveData
    val loggedOutLiveData = repository.loggedOutLiveData

    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password)
        }
    }

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }


}