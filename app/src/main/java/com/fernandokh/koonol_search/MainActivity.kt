package com.fernandokh.koonol_search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoonolsearchTheme {
                LaunchedEffect(true) {
                    delay(5000)
                }

                Surface(
                    color =  if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isSystemInDarkTheme()) R.drawable.b_logodark else R.drawable.b_logolight
                            ),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(300.dp)
                        )
                    }
                }
            }
        }
    }
}
