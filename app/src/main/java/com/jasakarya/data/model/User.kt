package com.jasakarya.data.model

data class User(
    val username: String = "",
    val email: String = "",
    val full_name: String = "",
    val gender: String = "",
    val date_of_birth: Birthdate = Birthdate(1, 1, 1970),
    val location: String = "",
    val preferredCategories: List<String> = listOf()
)

data class Birthdate(
    val day: Int = 1,
    val month: Int = 1,
    val year: Int = 1970
)
