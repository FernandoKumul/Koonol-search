package com.fernandokh.koonol_search.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fernandokh.koonol_search.ui.components.TopBarGoBack
import com.fernandokh.koonol_search.ui.components.maps.MapComponent
import com.fernandokh.koonol_search.utils.MarkLocation

@Composable
fun TianguisMapScreen(
    navHostController: NavHostController,
    latitude: Double,
    longitude: Double,
    name: String
) {
    Log.i("dev-debug", name)
    Scaffold { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapComponent(
                modifier = Modifier.fillMaxSize(),
                marks = listOf(MarkLocation(latitude, longitude, name)),
                isDraggable = false,
            )
            TopBarGoBack(navHostController)
        }
    }
}