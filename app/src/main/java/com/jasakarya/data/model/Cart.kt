package com.jasakarya.data.model

data class Cart(
    val cartId : String = "",
    val userEmail : String = "",
    val contentId : String = "",
    val contentName : String = "",
    val selectedPackage: Package = Package(),
    val contentPrice : Int = 0,
    val note: String = "",
    val imgUrl: String = ""
)