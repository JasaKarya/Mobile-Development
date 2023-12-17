package com.jasakarya.data.model

data class Biodata(
    val username: String = "",
    val email: String = "",
    val full_name: String = "",
    val gender: String = "",
    val date_of_birth: Birthdate,
    val location: String = ""
)
