package com.fernandokh.koonol_search.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernandokh.koonol_search.data.RetrofitInstance
import com.fernandokh.koonol_search.data.api.SaleStallApiService
import com.fernandokh.koonol_search.data.models.SalesStallFullModel
import com.fernandokh.koonol_search.utils.evaluateHttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SalesStallInfoViewModel: ViewModel() {
    private val apiService = RetrofitInstance.create(SaleStallApiService::class.java)

    private val _isSaleStall = MutableStateFlow<SalesStallFullModel?>(null)
    val isSaleStall: StateFlow<SalesStallFullModel?> = _isSaleStall

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getSaleStall(promotionId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response =
                    apiService.getById(promotionId)
                _isSaleStall.value = response.data
                Log.i("dev-debug", "Puesto obtenido con éxito")
            } catch (e: HttpException) {
                val messageError = evaluateHttpException(e)
                Log.e("dev-debug", "Error al obtener el puesto: $messageError")
                _isSaleStall.value = null
            } catch (e: Exception) {
                Log.e("dev-debug", e.message ?: "Ha ocurrido un error")
                _isSaleStall.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}