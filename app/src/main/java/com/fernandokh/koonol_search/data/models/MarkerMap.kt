package com.fernandokh.koonol_search.data.models

import com.google.gson.annotations.SerializedName

data class MarkerMap(
    @SerializedName("type")
    val type: String,
    @SerializedName("coordinates")
    val coordinates: List<Double>
)