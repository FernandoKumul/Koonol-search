package com.fernandokh.koonol_search.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme

@Composable
fun TopBarGoBack(navController: NavHostController, modifier: Modifier = Modifier) {
    var canNavigate by remember { mutableStateOf(true) }

    Row(modifier.fillMaxWidth().padding(16.dp, 20.dp)) {
        IconButton(
            onClick = {
                if (canNavigate) {
                    canNavigate = false
                    navController.popBackStack()
                }
            }, enabled = canNavigate,
            modifier = Modifier.clip(shape = CircleShape).background(MaterialTheme.colorScheme.outline)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PrevTopBarMenuTitle() {
    val navController = rememberNavController()
    KoonolsearchTheme(dynamicColor = false) {
        TopBarGoBack(navController)
    }
}