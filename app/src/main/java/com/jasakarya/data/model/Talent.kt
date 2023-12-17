package com.jasakarya.data.model

data class Talent(
    val avg_rating: Double,
    val content: Content,
    val image_url: String,
    val talent_brief: String,
    val talent_id: String,
    val talent_name: String
)

data class Content(
    val categories: Map<String, Int>,
    val content_id: Int,
    val content_name: String,
    val description: String,
    val image_url: String,
    val packages: List<Package>,
    val rating: Double
)

data class Package(
    val package_id: String,
    val package_name: String,
    val package_price: Int
)