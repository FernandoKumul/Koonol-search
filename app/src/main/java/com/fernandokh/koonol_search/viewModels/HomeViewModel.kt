package com.fernandokh.koonol_search.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.data.RetrofitInstance
import com.fernandokh.koonol_search.data.api.TianguisApiService
import com.fernandokh.koonol_search.data.models.TianguisModel
import com.fernandokh.koonol_search.utils.evaluateHttpException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel() {
    private val apiServiceTinaguis = RetrofitInstance.create(TianguisApiService::class.java)

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    private val _isValueSearch = MutableStateFlow("")
    val isValueSearch: StateFlow<String> = _isValueSearch

    private val _isHistoryList = MutableStateFlow<List<String>>(emptyList())
    val isHistoryList: StateFlow<List<String>> = _isHistoryList

    private val _dataStoreManager = MutableStateFlow<DataStoreManager?>(null)

    private val _navigationEvent = Channel<String>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _isLoadingTianguis = MutableStateFlow(true)
    val isLoadingTianguis: StateFlow<Boolean> = _isLoadingTianguis

    private val _isListTianguis = MutableStateFlow<List<TianguisModel>>(emptyList())
    val isListTianguis: StateFlow<List<TianguisModel>> = _isListTianguis

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun resetToastMessage() {
        _toastMessage.value = null
    }

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

    fun getTianguis() {
        viewModelScope.launch {
            try {
                _isLoadingTianguis.value = true
                val response = apiServiceTinaguis.search("", 1, 3, "newest")
                _isListTianguis.value = response.data?.results ?: emptyList()
                Log.i("dev-debug", "Tianguis obtenidos exitosamente")
            } catch (e: HttpException) {
                val errorMessage = evaluateHttpException(e)
                Log.e("dev-debug", "Error api: $errorMessage")
                showToast(errorMessage)
            } catch (e: Exception) {
                Log.i("dev-debug", e.message ?: "Ha ocurrido un error")
                showToast("Ocurrió un error al obtener los tianguis")
            } finally {
                _isLoadingTianguis.value = false
            }
        }
    }
}