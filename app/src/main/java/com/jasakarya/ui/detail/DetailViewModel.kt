package com.jasakarya.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.model.Cart
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository): ViewModel(){

    val talent = repository.talentLiveData

    val cartPushSuccess = repository.cartPushSuccesss

    fun getTalent(talentId: Int) {
        viewModelScope.launch {
            repository.fetchTalentByContentId(talentId)
        }
    }

    fun pushCart(cart: Cart) {
        viewModelScope.launch {
            repository.addCart(cart)
        }
    }
}