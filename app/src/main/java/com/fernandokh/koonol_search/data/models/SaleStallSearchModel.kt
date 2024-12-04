package com.fernandokh.koonol_search.data.models

import com.google.gson.annotations.SerializedName

data class SaleStallSearchModel(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val photos: List<String>,
    val type: Boolean,
    val active: Boolean,
    val subcategory: Subcategory,
    val category: Category,
    val hasActivePromotions: Boolean,
    val creationDate: String
)

data class Subcategory(
    @SerializedName("_id")
    val id: String,
    val name: String,
)

data class Category(
    @SerializedName("_id")
    val id: String,
    val name: String,
)
