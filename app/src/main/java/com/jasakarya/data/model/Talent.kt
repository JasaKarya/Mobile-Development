package com.jasakarya.data.model

import android.os.Parcelable


data class Talent(
    val avg_rating: Double = 0.0,
    val content: Content = Content(),
    val image_url: String = "",
    val talent_brief: String = "",
    val talent_id: String = "",
    val talent_name: String = ""
)

data class Content(
    val categories: Map<String, Int> = mapOf(),
    val content_id: Int = 1,
    val content_name: String = "",
    val description: String = "",
    val image_url: String = "",
    val packages: List<Package> = listOf(),
    val rating: Double = 0.0
)

data class Package(
    val package_id: String = "",
    val package_name: String = "",
    val package_price: Int = 0
)

