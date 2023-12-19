package com.jasakarya.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository): ViewModel() {

        val user = repository.userLiveData

        fun getUser(email: String) {
            viewModelScope.launch {
                repository.fetchUserByEmail(email)
            }

        }
}