package com.jasakarya.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasakarya.data.repository.Repository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: Repository): ViewModel() {

    val carts = repository.cartsLiveData

    val cartDeleteSuccess = repository.cartDeleteSuccessLiveData

    fun getCarts(userEmail: String) {
        viewModelScope.launch {
            repository.fetchCartsByUserEmail(userEmail)
        }
    }

    fun deleteCart(cartId: String) {
        viewModelScope.launch {
            repository.deleteCartByCartId(cartId)
        }
    }
}