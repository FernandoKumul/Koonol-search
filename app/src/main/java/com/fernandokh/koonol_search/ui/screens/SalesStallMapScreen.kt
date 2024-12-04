package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fernandokh.koonol_search.ui.components.TopBarGoBack
import com.fernandokh.koonol_search.ui.components.maps.MapComponent
import com.fernandokh.koonol_search.viewModels.SalesStallMapViewModel

@Composable
fun SalesStallMapScreen(navHostController: NavHostController, viewModel: SalesStallMapViewModel) {
    val marks by viewModel.isLocations.collectAsState()

    Scaffold { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (marks.isNotEmpty()) {
                MapComponent(
                    modifier = Modifier.fillMaxSize(),
                    marks = marks,
                    zoom = 20f,
                    isDraggable = false,
                )
            }
            TopBarGoBack(navHostController)
        }
    }

}