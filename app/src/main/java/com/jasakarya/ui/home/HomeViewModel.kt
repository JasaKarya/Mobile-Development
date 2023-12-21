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

    val userSetPreference = repository.userHasPreferencesLiveData

    val userLiveData = repository.userLiveData

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

    fun searchContents(query: String, limit: Int) {
        viewModelScope.launch {
            repository.searchContentsByQuery(query, limit)
        }
    }

    fun getSortedContents(limit:Int){
        viewModelScope.launch {
            repository.fetchAllContentsSortedByRating(limit)
        }
    }

    fun getTalents(limit: Int) {
        viewModelScope.launch {
            repository.fetchTalents(limit)
        }
    }



    fun checkIfUserPreferred(email: String) {
        viewModelScope.launch {
            repository.checkIfUserPreferred(email)
        }
    }

    fun getUser(email: String) {
        viewModelScope.launch {
            repository.fetchUserByEmail(email)
        }
    }

}
