package com.fernandokh.koonol_search.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fernandokh.koonol_search.R
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.ui.components.CustomSearchBar
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.Screen
import com.fernandokh.koonol_search.viewModels.HomeViewModel
import kotlinx.coroutines.flow.first

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    dataStoreManager: DataStoreManager,
    viewModel: HomeViewModel = viewModel()
) {
    val isValueSearch by viewModel.isValueSearch.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setDataStoreManager(dataStoreManager)
        viewModel.setHistoryList(dataStoreManager.getHistoryList().first())
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            Log.i("dev-debug", "Event: $event")
            navHostController.navigate(Screen.Search.createRoute(event))
        }
    }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp, 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("¿Qué estás buscando?", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                CustomSearchBar(
                    text = isValueSearch,
                    placeholder = "Buscar",
                    onSearch = { viewModel.searchSalesStall() },
                    onChange = { viewModel.changeValueSearch(it) }
                )
            }

            HistorySearchList(viewModel)
        }
    }
}

@Composable
private fun HistorySearchList(viewModel: HomeViewModel) {
    val isHistoryList by viewModel.isHistoryList.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Busquedas recientes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        if (isHistoryList.isEmpty()) {
            Text(
                "Sin busquedas recientes",
                Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            isHistoryList.forEachIndexed { index, log ->
                if (index >= 3) return@forEachIndexed

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_history_24),
                        contentDescription = "ic_clear_time",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        log,
                        Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    IconButton(onClick = { viewModel.removeItemHistoryList(index) }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "ic_clear")
                    }
                }
            }

        }

    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PrevHomeScreen() {
    val navHostController = rememberNavController()
    val dataStoreManager = DataStoreManager(LocalContext.current)

    KoonolsearchTheme(dynamicColor = false) {
        HomeScreen(navHostController, dataStoreManager)
    }
}