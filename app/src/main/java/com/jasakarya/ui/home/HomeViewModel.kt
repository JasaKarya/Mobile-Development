package com.jasakarya.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository): ViewModel(){

    val contents = repository.contentsLiveData

    val talents = repository.talentsLiveData

    val userHasPreferences = repository.userHasPreferencesLiveData

    val preferenceCreated = repository.preferenceCreatedLiveData

    fun getContents(limit: Int) {
        viewModelScope.launch {
            repository.fetchContents(limit)
        }
    }

    fun getFilteredContents(categories: List<String>, minRating: Double, limit: Int) {
        viewModelScope.launch {
            repository.fetchFilteredContents(categories, minRating, limit)
        }
    }

    fun getTalents(limit: Int) {
        viewModelScope.launch {
            repository.fetchTalents(limit)
        }
    }

    fun getUserHasPreferences(email: String) {
        viewModelScope.launch {
            repository.checkUserPreference(email)
        }
    }

    fun createPreference(userEmail: String, preferences: Map<String, Int>) {
        viewModelScope.launch {
            repository.createUserPreference(userEmail, preferences)
        }
    }

}
