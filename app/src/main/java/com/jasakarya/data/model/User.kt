package com.jasakarya.data.model

data class User(
    val username: String,
    val password: String,
    val email: String,
    val full_name: String,
    val gender: String,
    val date_of_birth: Birthdate,
    val location: String
)

data class Birthdate(
    val day: Int,
    val month: Int,
    val year: Int
)