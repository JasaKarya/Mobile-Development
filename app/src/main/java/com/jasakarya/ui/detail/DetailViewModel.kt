package com.jasakarya.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository): ViewModel(){

    val talent = repository.talentLiveData

    fun getTalent(talentId: Int) {
        viewModelScope.launch {
            repository.fetchTalentByContentId(talentId)
        }
    }
}