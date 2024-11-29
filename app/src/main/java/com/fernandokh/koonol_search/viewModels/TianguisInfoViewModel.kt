package com.fernandokh.koonol_search.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernandokh.koonol_search.data.RetrofitInstance
import com.fernandokh.koonol_search.data.api.SaleStallApiService
import com.fernandokh.koonol_search.data.api.TianguisApiService
import com.fernandokh.koonol_search.data.models.SaleStallNamePhotoModel
import com.fernandokh.koonol_search.data.models.TianguisModel
import com.fernandokh.koonol_search.utils.evaluateHttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class TianguisInfoViewModel : ViewModel() {
    private val apiService = RetrofitInstance.create(TianguisApiService::class.java)
    private val apiServiceSaleStall = RetrofitInstance.create(SaleStallApiService::class.java)

    private val _isTianguis = MutableStateFlow<TianguisModel?>(null)
    val isTianguis: StateFlow<TianguisModel?> = _isTianguis

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSaleStalls = MutableStateFlow<List<SaleStallNamePhotoModel>>(emptyList())
    val isSaleStalls: StateFlow<List<SaleStallNamePhotoModel>> = _isSaleStalls

    private val _isLoadingSaleStalls = MutableStateFlow(true)
    val isLoadingSaleStall: StateFlow<Boolean> = _isLoadingSaleStalls

    fun getTianguis(tianguisId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response =
                    apiService.getById(tianguisId)
                _isTianguis.value = response.data
                Log.i("dev-debug", "Tianguis obtenido con éxito")
            } catch (e: HttpException) {
                val messageError = evaluateHttpException(e)
                Log.e("dev-debug", "Error al obtener el puesto: $messageError")
                _isTianguis.value = null
            } catch (e: Exception) {
                Log.e("dev-debug", e.message ?: "Ha ocurrido un error")
                _isTianguis.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAllSaleStalls(tianguisId: String) {
        viewModelScope.launch {
            try {
                _isLoadingSaleStalls.value = true
                val response =
                    apiServiceSaleStall.getAllByTianguisId(tianguisId)
                if (response.data != null) _isSaleStalls.value = response.data
                Log.i("dev-debug", "Puestos obtenidos con éxito")
            } catch (e: HttpException) {
                val messageError = evaluateHttpException(e)
                Log.e("dev-debug", "Error al obtener los puestos del tianguis: $messageError")
                _isSaleStalls.value = emptyList()
            } catch (e: Exception) {
                Log.e("dev-debug", e.message ?: "Ha ocurrido un error")
                _isSaleStalls.value = emptyList()
            } finally {
                _isLoadingSaleStalls.value = false
            }
        }
    }
}