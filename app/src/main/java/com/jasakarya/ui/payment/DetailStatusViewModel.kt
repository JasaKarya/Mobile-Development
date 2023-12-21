package com.jasakarya.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class DetailStatusViewModel(private val repository: Repository): ViewModel()  {

    val transactionsList = repository.transactionsLiveData

    val transaction = repository.transactionLiveData

    val talent = repository.talentLiveData

    fun getTransactions(userEmail: String) {
        viewModelScope.launch{
            repository.fetchTransactionsByUserEmail(userEmail)
        }
    }

    fun getTransaction(transactionId: String) {
        viewModelScope.launch {
            repository.fetchTransactionByTransactionId(transactionId)
        }
    }

    fun getTalent(contentId: Int) {
        viewModelScope.launch {
            repository.fetchTalentByContentId(contentId)
        }
    }

}