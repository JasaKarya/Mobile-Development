package com.jasakarya.data.model

data class Transaction(
    val transactionId: String = "",
    val useremail: String = "",
    val talentId: String ="",
    val talentName: String = "",
    val contentId: Int = 0,
    val contentName: String = "",
    val contentImgUrl: String = "",
    val selectedPackage: String= "",
    val selectedPrice: Int = 0,
    val note: String = "",
    val paymentMethod: String = "",
    val paymentDate: Date = Date()
)

data class Date(
    val second: Int = 0,
    val minute: Int = 0,
    val hour: Int = 0,
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0
)