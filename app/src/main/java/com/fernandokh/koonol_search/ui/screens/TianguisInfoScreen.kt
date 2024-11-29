package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.fernandokh.koonol_search.R
import com.fernandokh.koonol_search.data.models.SaleStallNamePhotoModel
import com.fernandokh.koonol_search.data.models.TianguisModel
import com.fernandokh.koonol_search.ui.components.TopBarGoBack
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.Screen
import com.fernandokh.koonol_search.ui.theme.ThemeLightOutline
import com.fernandokh.koonol_search.viewModels.TianguisInfoViewModel

@Composable
fun TianguisInfoScreen(
    navHostController: NavHostController,
    tianguisId: String?,
    viewModel: TianguisInfoViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isTianguis by viewModel.isTianguis.collectAsState()
    val isSaleStalls by viewModel.isSaleStalls.collectAsState()
    val isLoadingSaleStalls by viewModel.isLoadingSaleStall.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTianguis(tianguisId ?: "")
        viewModel.getAllSaleStalls(tianguisId ?: "")
    }

    Scaffold { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading || isLoadingSaleStalls -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                isTianguis == null -> {
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
                        ImageTianguis(isTianguis!!)
                        TianguisInformation(navHostController, isTianguis!!, isSaleStalls)
                        Spacer(Modifier.height(80.dp))
                    }
                    TopBarGoBack(navHostController)
                    BottomButton(
                        Modifier
                            .fillMaxWidth()
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(horizontal = 12.dp, vertical = 24.dp),
                        navHostController,
                        latitude = isTianguis!!.markerMap.coordinates[0],
                        longitude = isTianguis!!.markerMap.coordinates[1],
                        name = isTianguis!!.name
                    )
                    //Add list here after
                }
            }
        }
    }
}

@Composable
private fun TianguisInformation(
    navHostController: NavHostController,
    tianguis: TianguisModel,
    saleStalls: List<SaleStallNamePhotoModel>
) {
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
        if (tianguis.schedule.isEmpty()) {
            Text(
                text = "Sin horarios disponibles",
                modifier = Modifier
                    .padding(12.dp, 12.dp, 12.dp, 0.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                tianguis.schedule.forEach { item ->
                    Text("${item.dayWeek} ${item.startTime} - ${item.endTime}")
                }
            }
        }
        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_compass_line),
                contentDescription = "icon_compass",
            )
            Text(
                "Indicaciones",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(tianguis.indications)
        Spacer(Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_store_2_line),
                    contentDescription = "icon_calendar",
                )
                Text(
                    "Puestos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            TextButton(onClick = { }) {
//                Text("Ver all")
            }

        }

        if (saleStalls.isEmpty()) {
            Text(
                text = "Sin horarios disponibles",
                modifier = Modifier
                    .padding(12.dp, 12.dp, 12.dp, 0.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth(if (saleStalls.size > 1) 1f else 0.5f)
            ) {
                saleStalls.forEachIndexed { index, item ->
                    if (index > 1) return@forEachIndexed
                    ItemSaleStall(modifier = Modifier.weight(1f), navHostController, item)
                }

            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

//@Composable
//fun SaleStallsList() {
//        LazyVerticalStaggeredGrid (
//            columns = StaggeredGridCells.Fixed(3),
//            verticalItemSpacing = 4.dp,
//            horizontalArrangement = Arrangement.spacedBy(4.dp),
//            userScrollEnabled = false,
//            content = {
//                items(3) { _ ->
//                    AsyncImage(
//                        model = "https://res.cloudinary.com/dpnjtuswg/image/upload/v1722242725/dhof6oavp5dhoog3mot8.jpg",
//                        contentScale = ContentScale.Crop,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .aspectRatio(1f)
//                    )
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//}

@Composable
private fun BottomButton(
    modifier: Modifier, navHostController: NavHostController, latitude: Double,
    longitude: Double,
    name: String
) {
    Box(modifier) {
        ElevatedButton(
            onClick = {
                navHostController.navigate(
                    Screen.TianguisMap.createRoute(
                        latitude,
                        longitude,
                        name
                    )
                )
            },
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

@Composable
private fun ItemSaleStall(modifier: Modifier, navHostController: NavHostController, saleStall: SaleStallNamePhotoModel) {
    Box(
        modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable { navHostController.navigate(Screen.SalesStallInfo.createRoute(saleStall.id)) }) {
        AsyncImage(
            model = if (saleStall.photos.isNotEmpty()) saleStall.photos[0] else null,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            placeholder = painterResource(R.drawable.default_image),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFF323236).copy(0.85f))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                saleStall.name,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ImageTianguis(tianguis: TianguisModel) {
    Box(
        Modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer, shape = CardDefaults.shape)
    ) {
        AsyncImage(
            model = tianguis.photo,
            contentDescription = "img_sale_stall",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = painterResource(R.drawable.default_image)
        )
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xFF323236).copy(0.85f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Text(tianguis.name, color = Color.White)
            Row {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "ic_location",
                    tint = ThemeLightOutline
                )
                Text(tianguis.locality, color = ThemeLightOutline, fontSize = 15.sp)
            }
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