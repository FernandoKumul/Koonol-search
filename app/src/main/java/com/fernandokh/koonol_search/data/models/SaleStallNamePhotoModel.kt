package com.fernandokh.koonol_search.data.models

import com.google.gson.annotations.SerializedName

data class SaleStallNamePhotoModel(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val photos: List<String>
)