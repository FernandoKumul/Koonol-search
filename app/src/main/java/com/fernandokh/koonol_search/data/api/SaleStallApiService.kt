package com.fernandokh.koonol_search.data.api

import com.fernandokh.koonol_search.data.ApiResponse
import com.fernandokh.koonol_search.data.models.SaleStallSearchModel
import com.fernandokh.koonol_search.data.models.SalesStallFullModel
import com.fernandokh.koonol_search.data.models.SearchModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SaleStallApiService {
    @GET("sales-stalls/search-public")
    suspend fun search(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String,
    ): ApiResponse<SearchModel<SaleStallSearchModel>>

    @GET("sales-stalls/{id}")
    suspend fun getById(@Path("id") id: String): ApiResponse<SalesStallFullModel>
}