package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fernandokh.koonol_search.ui.components.TopBarGoBack
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme

@Composable
fun TianguisInfoScreen(navHostController: NavHostController, tianguisId: String?) {
    Scaffold { innerPadding ->
        Column (
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            TopBarGoBack(navHostController)
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewTianguisInfoScreen() {
    KoonolsearchTheme(dynamicColor = false) {
        val navController = rememberNavController()
        TianguisInfoScreen(navController, "id")
    }
}