package com.fernandokh.koonol_search.viewModels

import androidx.lifecycle.ViewModel
import com.fernandokh.koonol_search.data.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel: ViewModel() {
    val SALES_STALLS = "salesStalls"
    val TIANGUIS = "tianguis"

    private val _isValueSearch = MutableStateFlow("")
    val isValueSearch: StateFlow<String> = _isValueSearch

    private val _isTypeSearch = MutableStateFlow(SALES_STALLS)
    val isTypeSearch = _isTypeSearch

    private val _dataStoreManager = MutableStateFlow<DataStoreManager?>(null)

    fun setDataStoreManager (dataStoreManager: DataStoreManager) {
        _dataStoreManager.value = dataStoreManager
    }

    fun changeValueSearch(newValue: String) {
        _isValueSearch.value = newValue
    }

    fun onChangeTypeSearch(newType: String) {
        _isTypeSearch.value = newType
    }
}