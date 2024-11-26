package com.fernandokh.koonol_search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.fernandokh.koonol_search.ui.theme.AppNavHost
import com.fernandokh.koonol_search.ui.theme.KoonolsearchTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            KoonolsearchTheme(dynamicColor = false) {
                LaunchedEffect(true) {
                    delay(5000)
                }

                Surface(
                    color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                    ) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            MyApp()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    AppNavHost(navHostController = navController)
}
