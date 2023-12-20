package com.jasakarya.ui.auth.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: Repository): ViewModel() {

    val userHasPreferences = repository.userHasPreferencesLiveData

    val preferenceCreated = repository.updatePreferredCategoriesStatus

        fun createPreference(email: String,categories: List<String>){
            viewModelScope.launch {
                repository.updatePreferredCategories(email, categories)
            }
        }
}