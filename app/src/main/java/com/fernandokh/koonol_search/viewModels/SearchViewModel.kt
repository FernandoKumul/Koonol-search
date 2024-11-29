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
import com.fernandokh.koonol_search.data.api.TianguisApiService
import com.fernandokh.koonol_search.data.api.TianguisModel
import com.fernandokh.koonol_search.data.models.SaleStallSearchModel
import com.fernandokh.koonol_search.data.pagingSource.SaleStallsPagingSource
import com.fernandokh.koonol_search.data.pagingSource.TianguisPagingSource
import com.fernandokh.koonol_search.utils.SelectOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    val SALES_STALLS = "salesStalls"
    val TIANGUIS = "tianguis"

    private val apiServiceSaleStall = RetrofitInstance.create(SaleStallApiService::class.java)
    private val apiServiceTianguis = RetrofitInstance.create(TianguisApiService::class.java)

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    private val _dataStoreManager = MutableStateFlow<DataStoreManager?>(null)

    private val _isValueSearch = MutableStateFlow("")
    val isValueSearch: StateFlow<String> = _isValueSearch

    private val _isTypeSearch = MutableStateFlow(SALES_STALLS)
    val isTypeSearch = _isTypeSearch

    //Pagination SaleStalls
    private val _isLoadingSalesStalls = MutableStateFlow(true)
    val isLoadingSalesStalls: StateFlow<Boolean> = _isLoadingSalesStalls

    private val _saleStallPagingFlow =
        MutableStateFlow<PagingData<SaleStallSearchModel>>(PagingData.empty())
    val saleStallPagingFlow: StateFlow<PagingData<SaleStallSearchModel>> = _saleStallPagingFlow

    private val _isTotalRecordsSalesStall = MutableStateFlow(0)
    val isTotalRecordsSalesStall: StateFlow<Int> = _isTotalRecordsSalesStall

    val optionsSortSaleStall = listOf(
        SelectOption("Más relevantes", "most-relevant"),
        SelectOption("Más nuevos", "newest"),
        SelectOption("Más viejos", "oldest"),
        SelectOption("A-Z", "a-z"),
        SelectOption("Z-A", "z-a"),
    )

    private val _isSortOptionSaleStall = MutableStateFlow(optionsSortSaleStall[0])
    val isSortOptionSaleStall: StateFlow<SelectOption> = _isSortOptionSaleStall

    private val _isFiltersDialogSaleStall = MutableStateFlow(false)
    val isFilterDialogSaleStall = _isFiltersDialogSaleStall

    //Pagination Tianguis
    val optionsSortTianguis = listOf(
        SelectOption("Más nuevos", "newest"),
        SelectOption("Más viejos", "oldest"),
        SelectOption("A-Z", "a-z"),
        SelectOption("Z-A", "z-a"),
    )

    private val _isLoadingTianguis = MutableStateFlow(true)
    val isLoadingTianguis: StateFlow<Boolean> = _isLoadingTianguis

    private val _tianguisPagingFlow =
        MutableStateFlow<PagingData<TianguisModel>>(PagingData.empty())
    val tianguisPagingFlow: StateFlow<PagingData<TianguisModel>> = _tianguisPagingFlow

    private val _isTotalRecordsTianguis = MutableStateFlow(0)
    val isTotalRecordsTianguis: StateFlow<Int> = _isTotalRecordsTianguis

    private val _isSortOptionTianguis = MutableStateFlow(optionsSortTianguis[0])
    val isSortOptionTianguis: StateFlow<SelectOption> = _isSortOptionTianguis

    private val _isFiltersDialogTianguis = MutableStateFlow(false)
    val isFilterDialogTianguis = _isFiltersDialogTianguis


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
            searchTianguis()
        }
    }

    fun changeFiltersSaleStalls(sort: SelectOption) {
        _isSortOptionSaleStall.value = sort
        searchSaleStalls()
    }

    fun changeFiltersTianguis(sort: SelectOption) {
        _isSortOptionTianguis.value = sort
        searchTianguis()
    }

    fun closeFiltersSaleStalls() {
        _isFiltersDialogSaleStall.value = false
    }

    fun openFiltersSaleStalls() {
        _isFiltersDialogSaleStall.value = true
    }

    fun closeFiltersTianguis() {
        _isFiltersDialogTianguis.value = false
    }

    fun openFiltersTianguis() {
        _isFiltersDialogTianguis.value = true
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
                        _isTotalRecordsSalesStall.value = it
                    }
                }.flow.cachedIn(viewModelScope)

            pager.collect { pagingData ->
                _saleStallPagingFlow.value = pagingData
                _isLoadingSalesStalls.value = false
            }
        }
    }

    fun searchTianguis() {
        _isLoadingTianguis.value = true
        viewModelScope.launch {
            val pager =
                Pager(PagingConfig(pageSize = 20, prefetchDistance = 3, initialLoadSize = 20)) {
                    TianguisPagingSource(
                        apiServiceTianguis,
                        _isValueSearch.value,
                        _isSortOptionTianguis.value.value,
                    ) {
                        _isTotalRecordsTianguis.value = it
                    }
                }.flow.cachedIn(viewModelScope)
            pager.collect { pagingData ->
                _tianguisPagingFlow.value = pagingData
                _isLoadingTianguis.value = false
            }
        }
    }
}