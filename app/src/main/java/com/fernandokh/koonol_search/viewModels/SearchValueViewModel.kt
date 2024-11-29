package com.fernandokh.koonol_search.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SearchValueViewModel : ViewModel() {
    private val _isValueSearch = MutableStateFlow("")

    fun getValueSearch(): String {
        return _isValueSearch.value
    }

    fun setValueSearch(newValue: String) {
        _isValueSearch.value = newValue
    }

}