package com.fernandokh.koonol_search.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.data.RetrofitInstance
import com.fernandokh.koonol_search.data.api.SaleStallApiService
import com.fernandokh.koonol_search.data.models.SaleStallSearchModel
import com.fernandokh.koonol_search.data.pagingSource.SaleStallsPagingSource
import com.fernandokh.koonol_search.utils.SelectOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    val SALES_STALLS = "salesStalls"
    val TIANGUIS = "tianguis"

    private val apiServiceSaleStall = RetrofitInstance.create(SaleStallApiService::class.java)

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    private val _dataStoreManager = MutableStateFlow<DataStoreManager?>(null)

    private val _isValueSearch = MutableStateFlow("")
    val isValueSearch: StateFlow<String> = _isValueSearch

    private val _isTypeSearch = MutableStateFlow(SALES_STALLS)
    val isTypeSearch = _isTypeSearch

    private val _isLoadingSalesStalls = MutableStateFlow(true)
    val isLoadingSalesStalls: StateFlow<Boolean> = _isLoadingSalesStalls

    //Pagination SaleStalls
    private val _saleStallPagingFlow =
        MutableStateFlow<PagingData<SaleStallSearchModel>>(PagingData.empty())
    val saleStallPagingFlow: StateFlow<PagingData<SaleStallSearchModel>> = _saleStallPagingFlow

    private val _isTotalRecords = MutableStateFlow(0)
    val isTotalRecords: StateFlow<Int> = _isTotalRecords

    val optionsSort = listOf(
        SelectOption("Más relevantes", "most-relevant"),
        SelectOption("Más nuevos", "newest"),
        SelectOption("Más viejos", "oldest"),
        SelectOption("A-Z", "a-z"),
        SelectOption("Z-A", "z-a"),
    )

    private val _isSortOptionSaleStall = MutableStateFlow(optionsSort[0])
    val isSortOptionSaleStall: StateFlow<SelectOption> = _isSortOptionSaleStall

    private val _isFiltersDialogSaleStall = MutableStateFlow(false)
    val isFilterDialogSaleStall = _isFiltersDialogSaleStall

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun resetToastMessage() {
        _toastMessage.value = null
    }

    fun setDataStoreManager (dataStoreManager: DataStoreManager) {
        _dataStoreManager.value = dataStoreManager
    }

    fun changeValueSearch(newValue: String) {
        _isValueSearch.value = newValue
    }

    fun onChangeTypeSearch(newType: String) {
        if (_isTypeSearch.value == newType) return

        _isTypeSearch.value = newType

        if (newType == SALES_STALLS) {
            searchSaleStalls()
        } else {
            //
        }
    }

    fun changeFiltersSaleStalls(sort: SelectOption) {
        _isSortOptionSaleStall.value = sort
        searchSaleStalls()
    }

    fun closeFiltersSaleStalls() {
        _isFiltersDialogSaleStall.value = false
    }

    fun openFiltersSaleStalls() {
        _isFiltersDialogSaleStall.value = true
    }

    fun searchSaleStalls() {
        _isLoadingSalesStalls.value = true
        viewModelScope.launch {
            val pager =
                Pager(PagingConfig(pageSize = 20, prefetchDistance = 3, initialLoadSize = 20)) {
                    SaleStallsPagingSource(
                        apiServiceSaleStall,
                        _isValueSearch.value,
                        _isSortOptionSaleStall.value.value,
                    ) {
                        _isTotalRecords.value = it
                    }
                }.flow.cachedIn(viewModelScope)

            pager.collect { pagingData ->
                _saleStallPagingFlow.value = pagingData
                _isLoadingSalesStalls.value = false
            }
        }
    }
}