package com.fernandokh.koonol_search.data.models

data class SearchModel<T>(
    val count: Int,
    val results: List<T>,
)

