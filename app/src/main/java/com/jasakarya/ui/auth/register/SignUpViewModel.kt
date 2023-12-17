package com.jasakarya.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.model.User
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: Repository): ViewModel(){

    val userLiveData = repository.userLiveData
    val loggedOutLiveData = repository.loggedOutLiveData

    fun register(user: User){
        viewModelScope.launch {
            repository.register(user)
        }
    }

}