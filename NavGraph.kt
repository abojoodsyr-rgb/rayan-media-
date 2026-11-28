package com.rayan.rayantube.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rayan.rayantube.ui.screens.DownloadsScreen
import com.rayan.rayantube.ui.screens.HomeScreen
import com.rayan.rayantube.ui.screens.PlayerScreen
import com.rayan.rayantube.ui.screens.SettingsScreen

object Routes {
    const val HOME = "home"
    const val DOWNLOADS = "downloads"
    const val SETTINGS = "settings"
    const val PLAYER = "player/{videoUri}"

    fun player(uri: Uri): String = "player/${Uri.encode(uri.toString())}"
}

@Composable
fun RayantubeNavGraph(
    navController: NavHostController,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Routes.HOME, modifier = modifier) {

        composable(Routes.HOME) {
            HomeScreen()
        }

        composable(Routes.DOWNLOADS) {
            DownloadsScreen(onOpenVideo = { video ->
                navController.navigate(Routes.player(video.uri))
            })
        }

        composable(
            route = Routes.PLAYER,
            arguments = listOf(navArgument("videoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("videoUri") ?: ""
            val uri = Uri.parse(Uri.decode(encoded))
            PlayerScreen(videoUri = uri)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                darkModeEnabled = darkModeEnabled,
                onDarkModeChange = onDarkModeChange
            )
        }
    }
}
