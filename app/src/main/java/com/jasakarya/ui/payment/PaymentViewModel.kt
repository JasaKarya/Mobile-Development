package com.jasakarya.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: Repository): ViewModel()  {

    val fetchedTalent = repository.talentLiveData


    fun getTalent(contentId: Int) {
        viewModelScope.launch {
            repository.fetchTalentByContentId(contentId)
        }
    }
}