package com.fernandokh.koonol_search.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.fernandokh.koonol_search.R
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.data.api.TianguisModel
import com.fernandokh.koonol_search.data.models.SaleStallSearchModel
import com.fernandokh.koonol_search.ui.components.CustomDialog
import com.fernandokh.koonol_search.ui.components.CustomSearchBar
import com.fernandokh.koonol_search.ui.components.CustomSelect
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.Screen
import com.fernandokh.koonol_search.viewModels.SearchViewModel

@Composable
fun SearchScreen(
    navHostController: NavHostController,
    query: String,
    dataStoreManager: DataStoreManager,
    viewModel: SearchViewModel = viewModel()
) {
    val isTypeSearch by viewModel.isTypeSearch.collectAsState()
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()


    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetToastMessage()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setDataStoreManager(dataStoreManager)
        viewModel.changeValueSearch(query)
        viewModel.searchSaleStalls()
    }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FiltersDialogSaleStalls(viewModel)
            FiltersDialogTianguis(viewModel)
            HeaderSearchBar(viewModel, isTypeSearch)
            if (isTypeSearch == viewModel.SALES_STALLS) {
                ListSalesStall(navHostController, viewModel)
            } else {
                ListTianguis(navHostController, viewModel)
            }
        }
    }
}

@Composable
private fun FiltersDialogSaleStalls(viewModel: SearchViewModel) {
    val isSortOption by viewModel.isSortOptionSaleStall.collectAsState()
    val isOpen by viewModel.isFilterDialogSaleStall.collectAsState()

    if (isOpen) {
        var sortOptionCurrent by remember { mutableStateOf(isSortOption) }

        CustomDialog(onDismissRequest = { viewModel.closeFiltersSaleStalls() }) {
            Text(
                "Filtros",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(10.dp))

            Text("Ordernar por", color = MaterialTheme.colorScheme.onPrimaryContainer)
            CustomSelect(options = viewModel.optionsSortSaleStall,
                selectedOption = sortOptionCurrent,
                onOptionSelected = { sortOptionCurrent = it })
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        sortOptionCurrent = viewModel.optionsSortSaleStall[0]
                    }
                ) {
                    Text("Restablecer", color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.changeFiltersSaleStalls(
                            sortOptionCurrent,
                        )
                        viewModel.closeFiltersSaleStalls()
                    }) {
                    Text("Aplicar")
                }
            }
        }
    }
}

@Composable
private fun FiltersDialogTianguis(viewModel: SearchViewModel) {
    val isSortOption by viewModel.isSortOptionTianguis.collectAsState()
    val isOpen by viewModel.isFilterDialogTianguis.collectAsState()

    if (isOpen) {
        var sortOptionCurrent by remember { mutableStateOf(isSortOption) }

        CustomDialog(onDismissRequest = { viewModel.closeFiltersTianguis() }) {
            Text(
                "Filtros",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(10.dp))

            Text("Ordernar por", color = MaterialTheme.colorScheme.onPrimaryContainer)
            CustomSelect(options = viewModel.optionsSortTianguis,
                selectedOption = sortOptionCurrent,
                onOptionSelected = { sortOptionCurrent = it })
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        sortOptionCurrent = viewModel.optionsSortTianguis[0]
                    }
                ) {
                    Text("Restablecer", color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.changeFiltersTianguis(
                            sortOptionCurrent,
                        )
                        viewModel.closeFiltersTianguis()
                    }) {
                    Text("Aplicar")
                }
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
            onSearch = { if (isTypeSearch == viewModel.SALES_STALLS) viewModel.searchSaleStalls() else viewModel.searchTianguis() },
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
private fun ListSalesStall(navHostController: NavHostController, viewModel: SearchViewModel) {
    val salesStalls = viewModel.saleStallPagingFlow.collectAsLazyPagingItems()
    val totalRecords by viewModel.isTotalRecordsSalesStall.collectAsState()
    val isLoadingSaleStalls by viewModel.isLoadingSalesStalls.collectAsState()

    when {
        //Carga inicial
        (salesStalls.loadState.refresh is LoadState.Loading || isLoadingSaleStalls) && salesStalls.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        //Estado vacio
        salesStalls.loadState.refresh is LoadState.NotLoading && salesStalls.itemCount == 0 -> {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron puestos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp, 8.dp, 12.dp, 0.dp)
                )
            }
        }

        //Error
        salesStalls.loadState.hasError -> {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "Ha ocurrido un error")
            }
        }

        else -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resultados: $totalRecords",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    IconButton(
                        onClick = { viewModel.openFiltersSaleStalls() }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(R.drawable.baseline_filter_list_alt_24),
                            contentDescription = "ic_filter",
                        )
                    }
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(salesStalls.itemCount) {
                        salesStalls[it]?.let { saleStall ->
                            ItemSalesStall(navHostController, saleStall)
                        }
                    }

                    when {
                        salesStalls.loadState.append is LoadState.NotLoading && salesStalls.loadState.append.endOfPaginationReached -> {
                            if (salesStalls.itemCount >= 5) {
                                item {
                                    Text(
                                        text = "Has llegado al final de la lista",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 12.dp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }

                        salesStalls.loadState.append is LoadState.Loading -> {
                            // Loader al final de la lista
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        salesStalls.loadState.append is LoadState.Error -> {
                            item {
                                Text(
                                    text = "Error al cargar más datos",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 12.dp),
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListTianguis(navHostController: NavHostController, viewModel: SearchViewModel) {
    val tianguis = viewModel.tianguisPagingFlow.collectAsLazyPagingItems()
    val totalRecords by viewModel.isTotalRecordsTianguis.collectAsState()
    val isLoadingTianguis by viewModel.isLoadingTianguis.collectAsState()

    when {
        //Carga inicial
        (tianguis.loadState.refresh is LoadState.Loading || isLoadingTianguis) && tianguis.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        //Estado vacio
        tianguis.loadState.refresh is LoadState.NotLoading && tianguis.itemCount == 0 -> {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron tianguis",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp, 8.dp, 12.dp, 0.dp)
                )
            }
        }

        //Error
        tianguis.loadState.hasError -> {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "Ha ocurrido un error")
            }
        }

        else -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resultados: $totalRecords",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    IconButton(
                        onClick = { viewModel.openFiltersTianguis() }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(R.drawable.baseline_filter_list_alt_24),
                            contentDescription = "ic_filter",
                        )
                    }
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tianguis.itemCount) {
                        tianguis[it]?.let { tianguis ->
                            ItemTianguis(navHostController, tianguis)
                        }
                    }

                    when {
                        tianguis.loadState.append is LoadState.NotLoading && tianguis.loadState.append.endOfPaginationReached -> {
                            if (tianguis.itemCount >= 5) {
                                item {
                                    Text(
                                        text = "Has llegado al final de la lista",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 12.dp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }

                        tianguis.loadState.append is LoadState.Loading -> {
                            // Loader al final de la lista
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        tianguis.loadState.append is LoadState.Error -> {
                            item {
                                Text(
                                    text = "Error al cargar más datos",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 12.dp),
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemSalesStall(navHostController: NavHostController, saleStall: SaleStallSearchModel) {
    OutlinedCard(
        onClick = {
            navHostController.navigate(Screen.SalesStallInfo.createRoute(saleStall.id)) {
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
                    text = saleStall.name,
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
                        text = saleStall.category.name
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
                        text = saleStall.subcategory.name
                    )
                }
            }
            AsyncImage(
                model = if (saleStall.photos.isNotEmpty()) saleStall.photos[0] else null,
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
private fun ItemTianguis(navHostController: NavHostController, tianguis: TianguisModel) {
    OutlinedCard(
        onClick = {
            navHostController.navigate(Screen.TianguisInfo.createRoute(tianguis.id)) {
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
                    text = tianguis.name,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "ic_location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        tianguis.locality ?: "Sin localidad",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
            }
            AsyncImage(
                model = tianguis.photo,
                contentDescription = "img_photo",
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

