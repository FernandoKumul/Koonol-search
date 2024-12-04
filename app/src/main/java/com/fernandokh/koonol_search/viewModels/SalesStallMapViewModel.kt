package com.fernandokh.koonol_search.viewModels

import androidx.lifecycle.ViewModel
import com.fernandokh.koonol_search.data.models.SalesStallFullModel
import com.fernandokh.koonol_search.utils.MarkLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SalesStallMapViewModel : ViewModel() {

    private val _isLocations = MutableStateFlow<List<MarkLocation>>(emptyList())
    val isLocations: StateFlow<List<MarkLocation>> = _isLocations


    fun setLocations(saleStall: SalesStallFullModel) {
        val marks = saleStall.locations.map {
            MarkLocation(
                latitude = it.markerMap.coordinates[1],
                longitude = it.markerMap.coordinates[0],
                saleStall.name
            )
        }
        _isLocations.value = marks
    }
}