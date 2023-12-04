package com.jasakarya.data.response

import com.google.gson.annotations.SerializedName

data class Responses(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
)