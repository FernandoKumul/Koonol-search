package com.fernandokh.koonol_search.data.api

import com.fernandokh.koonol_search.data.ApiResponse
import com.fernandokh.koonol_search.data.models.SearchModel
import com.fernandokh.koonol_search.data.models.TianguisModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TianguisApiService {
    @GET("tianguis")
    suspend fun search(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String
    ): ApiResponse<SearchModel<TianguisModel>>

    @GET("tianguis/{id}")
    suspend fun getById(@Path("id") id: String): ApiResponse<TianguisModel>
}