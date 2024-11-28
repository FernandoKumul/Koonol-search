package com.fernandokh.koonol_search.ui.screens

import android.content.ClipData.Item
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.fernandokh.koonol_search.R
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.ui.components.CustomSearchBar
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.Screen
import com.fernandokh.koonol_search.ui.theme.ThemeLightOutline
import com.fernandokh.koonol_search.viewModels.SearchViewModel
import org.jetbrains.annotations.Debug

@Composable
fun SearchScreen(
    navHostController: NavHostController,
    query: String,
    dataStoreManager: DataStoreManager,
    viewModel: SearchViewModel = viewModel()
) {
    val isTypeSearch by viewModel.isTypeSearch.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setDataStoreManager(dataStoreManager)
        viewModel.changeValueSearch(query)
        //Search API
    }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HeaderSearchBar(viewModel, isTypeSearch)
            if (isTypeSearch == viewModel.SALES_STALLS) {
                ListSalesStall(navHostController)
            } else {
                ListTianguis(navHostController)
            }
        }
    }
}


@Composable
private fun HeaderSearchBar(
    viewModel: SearchViewModel,
    isTypeSearch: String
) {
    val isValueSearch by viewModel.isValueSearch.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSearchBar(
            text = isValueSearch,
            placeholder = "Buscar",
            onSearch = { },
            onChange = { viewModel.changeValueSearch(it) }
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val btnColorDeselect = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = { viewModel.onChangeTypeSearch(viewModel.SALES_STALLS) },
                colors = if (isTypeSearch == viewModel.SALES_STALLS) ButtonDefaults.buttonColors() else btnColorDeselect
            ) {
                Text("Puestos")
            }

            Button(
                onClick = { viewModel.onChangeTypeSearch(viewModel.TIANGUIS) },
                colors = if (isTypeSearch == viewModel.TIANGUIS) ButtonDefaults.buttonColors() else btnColorDeselect
            ) {
                Text("Tianguis")
            }
        }
    }
}

@Composable
private fun ListSalesStall(navHostController: NavHostController) {
    Column(Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        Row(
            Modifier
                .fillMaxWidth().padding(bottom = 12.dp, top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Resultados: 00",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            IconButton(
                onClick = { }
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.baseline_filter_list_alt_24),
                    contentDescription = "ic_filter",
                )
            }
        }
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ItemSalesStall(navHostController)
            ItemSalesStall(navHostController)
            ItemSalesStall(navHostController)
        }
    }
}

@Composable
private fun ListTianguis(navHostController: NavHostController) {
    Column(Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        Row(
            Modifier
                .fillMaxWidth().padding(bottom = 12.dp, top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Resultados: 00",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            IconButton(
                onClick = { }
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.baseline_filter_list_alt_24),
                    contentDescription = "ic_filter",
                )
            }
        }
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ItemTianguis(navHostController)
            ItemTianguis(navHostController)
            ItemTianguis(navHostController)
        }
    }
}

@Composable
private fun ItemSalesStall(navHostController: NavHostController) {
    OutlinedCard(
        onClick = {
            navHostController.navigate(Screen.SalesStallInfo.createRoute("12345")) {
                launchSingleTop = true
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            Modifier.padding(16.dp, 14.dp),
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    text = "Nombre del Puesto",
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Box(
                    Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp, 4.dp)
                ) {
                    Text(
                        fontSize = 15.sp,
                        text = "Categoría"
                    )
                }
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp, 4.dp)
                ) {
                    Text(
                        fontSize = 15.sp,
                        text = "Sub-Categoría"
                    )
                }
            }
            AsyncImage(
                model = "https://res.cloudinary.com/dpnjtuswg/image/upload/v1722242725/dhof6oavp5dhoog3mot8.jpg",
                contentDescription = "img_user",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.default_image),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(72.dp)
            )
        }
    }
}

@Composable
private fun ItemTianguis(navHostController: NavHostController) {
    OutlinedCard(
        onClick = {
            navHostController.navigate(Screen.TianguisInfo.createRoute("12345")) {
                launchSingleTop = true
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            Modifier.padding(16.dp, 14.dp),
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    text = "Nombre del Tianguis",
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "ic_location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Cancún",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
            }
            AsyncImage(
                model = "https://res.cloudinary.com/dpnjtuswg/image/upload/v1722242725/dhof6oavp5dhoog3mot8.jpg",
                contentDescription = "img_user",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.default_image),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(72.dp)
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PrevHomeScreen() {
    val navHostController = rememberNavController()
    val dataStoreManager = DataStoreManager(LocalContext.current)

    KoonolsearchTheme(dynamicColor = false) {
        SearchScreen(navHostController, "", dataStoreManager)
    }
}

