package com.fernandokh.koonol_search.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.fernandokh.koonol_search.R
import com.fernandokh.koonol_search.ui.components.TopBarGoBack
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import com.fernandokh.koonol_search.ui.theme.Screen
import com.fernandokh.koonol_search.ui.theme.ThemeLightOutline

@Composable
fun TianguisInfoScreen(navHostController: NavHostController, tianguisId: String?) {
    Scaffold { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ImageTianguis()
                TianguisInformation(navHostController)
                Spacer(Modifier.height(80.dp))
            }
            TopBarGoBack(navHostController)
//            BottomButton(
//                Modifier
//                    .fillMaxWidth()
//                    .align(
//                        Alignment.BottomCenter
//                    )
//                    .padding(horizontal = 12.dp, vertical = 24.dp),
//                navHostController
//            )
            //Add list here after
        }
    }
}

@Composable
private fun TianguisInformation(navHostController: NavHostController) {
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
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Lunes 4:00 pm - 9:00 am")
            Text("Lunes 4:00 pm - 9:00 am")
            Text("Lunes 4:00 pm - 9:00 am")
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
        Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque leo purus, aliquet eu dolor vitae, facilisis vulputate orci.")
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
                Text("Ver todo")
            }

        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ItemSaleStall(modifier = Modifier.weight(1f), navHostController)
            ItemSaleStall(modifier = Modifier.weight(1f), navHostController)
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
private fun ItemSaleStall(modifier: Modifier, navHostController: NavHostController) {
    Box(
        modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable { navHostController.navigate(Screen.SalesStallInfo.createRoute("id")) }) {
        AsyncImage(
            model = "https://res.cloudinary.com/dpnjtuswg/image/upload/v1722242725/dhof6oavp5dhoog3mot8.jpg",
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
                "Nombre del tianguis djfkd fjdkafj dksfjd as",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ImageTianguis() {
    Box(
        Modifier
            .height(400.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer, shape = CardDefaults.shape)
    ) {
        AsyncImage(
            model = "https://res.cloudinary.com/dpnjtuswg/image/upload/v1722242725/dhof6oavp5dhoog3mot8.jpg",
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
            Text("Nombre del tianguis", color = Color.White)
            Row {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "ic_location",
                    tint = ThemeLightOutline
                )
                Text("Cancún", color = ThemeLightOutline, fontSize = 15.sp)
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