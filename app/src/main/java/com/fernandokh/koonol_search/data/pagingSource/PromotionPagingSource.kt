package com.fernandokh.koonol_search.data.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fernandokh.koonol_search.data.api.SaleStallApiService
import com.fernandokh.koonol_search.data.models.SaleStallSearchModel
import com.fernandokh.koonol_search.utils.evaluateHttpException
import retrofit2.HttpException

class SaleStallsPagingSource (
    private val apiService: SaleStallApiService,
    private val search: String,
    private val sort: String,
    private val onUpdateTotal: (Int) -> Unit
) : PagingSource<Int, SaleStallSearchModel>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SaleStallSearchModel> {
        return try {
            val currentPage = params.key ?: 1

            val response = apiService.search(
                page = currentPage,
                limit = params.loadSize,
                search = search,
                sort = sort,
            )

            val categories = response.data?.results ?: emptyList()
            onUpdateTotal(response.data?.count ?: 0)

            LoadResult.Page(
                data = categories,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (categories.isEmpty()) null else currentPage + 1
            )
        } catch (e: HttpException) {
            val errorMessage = evaluateHttpException(e)
            Log.e("dev-debug", "Error paginación: $errorMessage")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("dev-debug", "Error paginación ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SaleStallSearchModel>): Int? {
        return state.anchorPosition
    }
}