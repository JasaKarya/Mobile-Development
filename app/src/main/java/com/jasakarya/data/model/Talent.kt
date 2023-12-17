package com.jasakarya.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Talent(
    val talentId: String,
    val talentName: String,
    val talentBrief: String,
    val profileUrl: String,
    val avgRating: Float,
    val content: List<Content>
) : Parcelable

@Parcelize
data class Content(
    val contentId: String,
    val contentName: String,
    val description: String,
    val categories: List<String>,
    val imageUrl: String,
    val orderForm: String,
    val rating: Float,
    val orderPackage: List<OrderPackage>
) : Parcelable

@Parcelize
data class OrderPackage(
    val orderPackageId: Int,
    val orderPackageName: String,
    val orderPackagePrice: Float,
) : Parcelable
