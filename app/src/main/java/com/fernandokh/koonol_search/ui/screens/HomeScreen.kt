package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.fernandokh.koonol_search.R
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.ui.components.CustomSearchBar
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.Screen
import com.fernandokh.koonol_search.ui.theme.ThemeLightOutline
import com.fernandokh.koonol_search.viewModels.HomeViewModel
import com.fernandokh.koonol_search.viewModels.SearchValueViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    dataStoreManager: DataStoreManager,
    viewModel: HomeViewModel = viewModel(),
    searchValueViewModel: SearchValueViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.changeValueSearch("")
        viewModel.setDataStoreManager(dataStoreManager)
        viewModel.setHistoryList(dataStoreManager.getHistoryList().first())
        viewModel.getTianguis()
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            searchValueViewModel.setValueSearch(event)
            navHostController.navigate(Screen.Search.route)
        }
    }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSearchBar(viewModel)
            HistorySearchList(viewModel)
            SliderTianguis(viewModel, navHostController)
        }
    }
}

@Composable
private fun HeaderSearchBar(
    viewModel: HomeViewModel
) {
    val isValueSearch by viewModel.isValueSearch.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp, 24.dp),
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
}

@Composable
private fun HistorySearchList(viewModel: HomeViewModel) {
    val isHistoryList by viewModel.isHistoryList.collectAsState()

    if (isHistoryList.isNotEmpty()) {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp, 16.dp),
        ) {
            Text(
                "Busquedas recientes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            isHistoryList.forEachIndexed { index, log ->
                if (index >= 3) return@forEachIndexed

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .clickable { viewModel.searchSalesStallWithHistory(log, index) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history_24),
                            contentDescription = "ic_clear_time",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = log,
                            Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { viewModel.removeItemHistoryList(index) }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "ic_clear")
                    }
                }

            }
        }
    } else {
        Spacer(Modifier.height(16.dp))
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SliderTianguis(viewModel: HomeViewModel, navHostController: NavHostController) {
    val pagerState = rememberPagerState(initialPage = 0)
    val isLoadingTianguis by viewModel.isLoadingTianguis.collectAsState()
    val isTianguis by viewModel.isListTianguis.collectAsState()

    LaunchedEffect(Unit) {
        if (!isLoadingTianguis && isTianguis.isNotEmpty()) {
            while (true) {
                yield()
                delay(5000)
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % (pagerState.pageCount)
                )
            }
        }
    }

    Column(Modifier.padding(bottom = 16.dp)) {
        Text(
            "Tianguis recomendados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        )

        when {
            isLoadingTianguis -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            isTianguis.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron Tianguis",
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                HorizontalPager(
                    count = isTianguis.size,
                    state = pagerState,
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                ) { page ->
                    Card(
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                                lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
                        ) {
                            AsyncImage(
                                model = isTianguis[page].photo,
                                contentDescription = "img_sale_stall",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navHostController.navigate(
                                            Screen.TianguisInfo.createRoute(isTianguis[page].id)
                                        )
                                    },
                                placeholder = painterResource(R.drawable.default_image)
                            )
                            Column(
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(
                                        Color(0xFF323236).copy(0.85f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(isTianguis[page].name, color = Color.White)
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.LocationOn,
                                        contentDescription = "ic_location",
                                        tint = ThemeLightOutline
                                    )
                                    Text(
                                        isTianguis[page].locality ?: "Sin ubicación",
                                        color = ThemeLightOutline,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
    }

}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PrevHomeScreen() {
    val navHostController = rememberNavController()
    val dataStoreManager = DataStoreManager(LocalContext.current)

    KoonolsearchTheme(dynamicColor = false) {
        HomeScreen(navHostController, dataStoreManager)
    }
}