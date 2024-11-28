package com.fernandokh.koonol_search.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernandokh.koonol_search.data.DataStoreManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _isValueSearch = MutableStateFlow("")
    val isValueSearch: StateFlow<String> = _isValueSearch

    private val _isHistoryList = MutableStateFlow<List<String>>(emptyList())
    val isHistoryList: StateFlow<List<String>> = _isHistoryList

    private val _dataStoreManager = MutableStateFlow<DataStoreManager?>(null)

    private val _navigationEvent = Channel<String>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun setDataStoreManager (dataStoreManager: DataStoreManager) {
        _dataStoreManager.value = dataStoreManager
    }

    fun setHistoryList (list: List<String>) {
        _isHistoryList.value = list
    }

    fun removeItemHistoryList (index: Int) {
        val historyCopy = _isHistoryList.value.toMutableList()
        historyCopy.removeAt(index)
        _isHistoryList.value = historyCopy
        saveHistory()
    }

    fun changeValueSearch(newValue: String) {
        _isValueSearch.value = newValue
    }

    private fun addQueryInHistory() {
        _isHistoryList.update { listOf(_isValueSearch.value) + it }
    }

    fun searchSalesStall() {
        addQueryInHistory()
        saveHistory()
        viewModelScope.launch {
            _navigationEvent.send(_isValueSearch.value)
        }
    }

    private fun saveHistory() {
        if (_dataStoreManager.value != null) {
            viewModelScope.launch {
                _dataStoreManager.value!!.saveHistoryList(_isHistoryList.value)
            }
        }
    }

    fun searchSalesStallWithHistory(value: String, index: Int) {
        val historyCopy = _isHistoryList.value.toMutableList()
        historyCopy.removeAt(index)
        historyCopy.add(0, value)
        _isHistoryList.value = historyCopy
        saveHistory()
        viewModelScope.launch {
            _navigationEvent.send(value)
        }
    }
}