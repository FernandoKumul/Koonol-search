package com.fernandokh.koonol_search.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fernandokh.koonol_search.data.DataStoreManager
import com.fernandokh.koonol_search.ui.screens.HomeScreen
import com.fernandokh.koonol_search.ui.screens.SalesStallInfoScreen
import com.fernandokh.koonol_search.ui.screens.SalesStallMapScreen
import com.fernandokh.koonol_search.ui.screens.SearchScreen
import com.fernandokh.koonol_search.ui.screens.TianguisInfoScreen
import com.fernandokh.koonol_search.ui.screens.TianguisMapScreen
import com.fernandokh.koonol_search.viewModels.SearchValueViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")

    data object SalesStallInfo : Screen("sales-stalls/info/{id}") {
        fun createRoute(id: String) = "sales-stalls/info/$id"
    }
    data object SalesStallMap : Screen("sales-stalls/map/{id}") {
        fun createRoute(id: String) = "sales-stalls/map/$id"
    }

    data object TianguisInfo : Screen("tianguis/info/{id}") {
        fun createRoute(id: String) = "tianguis/info/$id"
    }
    data object TianguisMap : Screen("tianguis/map/{id}") {
        fun createRoute(id: String) = "tianguis/map/$id"
    }


}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier, navHostController: NavHostController, dataStoreManager: DataStoreManager
) {
    val valueSearchViewModel: SearchValueViewModel = viewModel()
    NavHost(navHostController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { HomeScreen(navHostController, dataStoreManager, searchValueViewModel = valueSearchViewModel) }
        composable(Screen.Search.route) { SearchScreen(navHostController, dataStoreManager, searchValueViewModel = valueSearchViewModel) }

        //Tianguis
        composable(
            Screen.TianguisInfo.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            TianguisInfoScreen(navHostController, backStackEntry.arguments?.getString("id"))
        }
        composable(
            Screen.TianguisMap.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            TianguisMapScreen(navHostController, backStackEntry.arguments?.getString("id"))
        }

        //Sales Stalls
        composable(
            Screen.SalesStallMap.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            SalesStallMapScreen(navHostController, backStackEntry.arguments?.getString("id"))
        }
        composable(
            Screen.SalesStallInfo.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            SalesStallInfoScreen(navHostController, backStackEntry.arguments?.getString("id"))
        }
    }
}