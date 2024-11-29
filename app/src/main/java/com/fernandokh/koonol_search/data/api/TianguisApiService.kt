package com.fernandokh.koonol_search.data.api

import com.fernandokh.koonol_search.data.ApiResponse
import com.fernandokh.koonol_search.data.models.SearchModel
import retrofit2.http.GET
import retrofit2.http.Query

interface TianguisApiService {
    @GET("tianguis")
    suspend fun search(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String
    ): ApiResponse<SearchModel<TianguisModel>>
}