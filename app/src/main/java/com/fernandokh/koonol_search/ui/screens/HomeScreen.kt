package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fernandokh.koonol_search.ui.theme.Screen

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Scaffold () { innerPadding ->
        Column (
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Hola a todos")
            Button(onClick = {
                navHostController.navigate(Screen.Search.createRoute(""))
            }) {
                Text("Buscar")
            }
        }
    }
}