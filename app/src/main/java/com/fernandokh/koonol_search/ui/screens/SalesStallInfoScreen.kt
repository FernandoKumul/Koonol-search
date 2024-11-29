package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import com.fernandokh.koonol_search.data.models.SalesStallFullModel
import com.fernandokh.koonol_search.ui.components.TopBarGoBack
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.ThemeDarkStatusDisabled
import com.fernandokh.koonol_search.ui.theme.ThemeDarkStatusEnabled
import com.fernandokh.koonol_search.ui.theme.ThemeLightStatusDisabled
import com.fernandokh.koonol_search.ui.theme.ThemeLightStatusEnabled
import com.fernandokh.koonol_search.viewModels.SalesStallInfoViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@Composable
fun SalesStallInfoScreen(
    navHostController: NavHostController,
    salesStallId: String?,
    viewModel: SalesStallInfoViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaleStall by viewModel.isSaleStall.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSaleStall(salesStallId ?: "")
    }

    Scaffold { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                isSaleStall == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontró el puesto",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(12.dp, 8.dp, 12.dp, 0.dp)
                        )
                    }
                }

                else -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ImagesSlider(isSaleStall!!.photos)
                        HeaderInformation(isSaleStall!!)
                        SalesStallInformation(isSaleStall!!)
                        Spacer(Modifier.height(80.dp))
                    }
                    BottomButton(
                        Modifier
                            .fillMaxWidth()
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(horizontal = 12.dp, vertical = 24.dp)
                    )
                }
            }
            TopBarGoBack(navHostController)

        }
    }
}

@Composable
private fun SalesStallInformation(saleStall: SalesStallFullModel) {
    Column(Modifier.padding(horizontal = 12.dp, vertical = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "icon_calendar",
            )
            Text(
                "Horarios",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        if (saleStall.locations.isEmpty()) {
            Text(
                text = "Sin horarios disponibles",
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                saleStall.locations.forEach { item ->
                    Text("${item.scheduleTianguisId.dayWeek} ${item.scheduleTianguisId.startTime} - ${item.scheduleTianguisId.endTime}")
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "icon_compass",
            )
            Text(
                "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(saleStall.description)
    }
}


@Composable
private fun HeaderInformation(saleStall: SalesStallFullModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 20.dp)
    ) {
        Text(
            saleStall.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))


        val colorStatus = when {
            isSystemInDarkTheme() && saleStall.active -> ThemeDarkStatusEnabled
            isSystemInDarkTheme() && !saleStall.active -> ThemeDarkStatusDisabled
            !isSystemInDarkTheme() && saleStall.active -> ThemeLightStatusEnabled
            else -> ThemeLightStatusDisabled
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(colorStatus)
                    .padding(12.dp, 4.dp)
            ) {
                Text(
                    fontSize = 15.sp,
                    text = if (saleStall.active) "Abierto" else "Cerrado"
                )
            }
            Box(
                Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp, 4.dp)
            ) {
                Text(
                    fontSize = 15.sp,
                    text = saleStall.subCategoryId.categoryId.name
                )
            }
            Box(
                Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp, 4.dp)
            ) {
                Text(
                    fontSize = 15.sp,
                    text = saleStall.subCategoryId.name
                )
            }
        }
    }
}

@Composable
fun BottomButton(modifier: Modifier) {
    Box(modifier) {
        ElevatedButton(
            onClick = { },
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Ubicación", fontSize = 20.sp)
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "ic_location",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ImagesSlider(photos: List<String>) {
    val pagerState = rememberPagerState(initialPage = 0)
    LaunchedEffect(Unit) {
        if (photos.isNotEmpty()) {
            while (true) {
                yield()
                delay(5000)
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % (pagerState.pageCount)
                )
            }
        }
    }

    Box(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        HorizontalPager(
            count = photos.size,
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
                ) {
                    AsyncImage(
                        model = photos[page],
                        contentDescription = "img_sale_stall",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = painterResource(R.drawable.default_image)
                    )
                }
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PrevSalesStallInfoScreen() {
    val navHostController = rememberNavController()

    KoonolsearchTheme(dynamicColor = false) {
        SalesStallInfoScreen(navHostController, "")
    }
}