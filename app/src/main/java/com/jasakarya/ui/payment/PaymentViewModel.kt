package com.jasakarya.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.model.Transaction
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: Repository): ViewModel()  {

    val fetchedTalent = repository.talentLiveData

    val addTransactionSuccess = repository.addTransactionSuccessLiveData

    val cartDeleteSuccess = repository.cartDeleteSuccessLiveData

    fun getTalent(contentId: Int) {
        viewModelScope.launch {
            repository.fetchTalentByContentId(contentId)
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.createTransaction(transaction)
        }
    }

    fun deleteCart(cartId: String) {
        viewModelScope.launch {
            repository.deleteCartByCartId(cartId)
        }
    }
}