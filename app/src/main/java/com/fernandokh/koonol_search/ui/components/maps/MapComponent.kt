package com.fernandokh.koonol_search.ui.components.maps

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.fernandokh.koonol_search.utils.MarkLocation
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    marks: List<MarkLocation>,
    zoom: Float = 15f, // Nivel de zoom inicial
    isDraggable: Boolean = false, // Determina si el marcador es arrastrable
) {
    // Estado del marcador inicial
    val markerPosition by remember { mutableStateOf(LatLng(marks[0].latitude, marks[0].longitude)) }

    // Configuración de la posición de la cámara
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, zoom)
    }

    // Renderizar el mapa
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        marks.forEach { item ->
            val markerState = rememberMarkerState(position = LatLng(item.latitude, item.longitude))

            Marker(
                state = markerState,
                title = item.title,
                draggable = isDraggable,
                onClick = {
                    // Opcional: Manejo de clic en el marcador
                    false
                }
            )
        }
    }
}
