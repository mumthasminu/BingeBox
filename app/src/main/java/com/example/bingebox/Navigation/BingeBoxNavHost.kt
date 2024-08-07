package com.example.bingebox

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bingebox.source.local.UserDao
import com.example.bingebox.Navigation.Routes
import com.example.bingebox.Screens.*
import com.example.bingebox.ui.Screens.HomeScreen
import com.example.bingebox.util.Constants
import com.example.bingebox.viewModel.ColorViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.bingebox.viewModel.LoadingViewModel

import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BingeBoxNavHost(userDao: UserDao) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val colorViewModel: ColorViewModel = viewModel()
    val backgroundColor by colorViewModel.backgroundColor.collectAsState()
    val loadingViewModel: LoadingViewModel = viewModel()

    LaunchedEffect(key1 = true) {
        delay(200)
        navController.navigate(Routes.signUpScreen)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Routes.signUpScreen) {


            composable(Routes.splashScreen) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.appicon),
                        contentDescription = "Splash Screen Logo"
                    )
                }
            }
            composable(Routes.signUpScreen) {
                SignUpScreen(navController, userDao = userDao,loadingViewModel)
            }
            composable(Routes.loginScreen) {
                LoginScreen(navController, userDao = userDao,loadingViewModel)
            }
            composable(Routes.homeScreen) {
                HomeScreen(navController, context, backgroundColor,loadingViewModel)
            }
            composable(Routes.searchResultsScreen) { backStackEntry ->
                val searchQuery = backStackEntry.arguments?.getString("searchQuery") ?: ""
                SearchResultsScreen(
                    apiKey = Constants.TMDB_API_KEY,
                    searchQuery = searchQuery,
                    navController = navController,
                    context = context

                )
            }

                composable("TVShowDetail/{tvShowId}") { backStackEntry ->
                    val tvShowId = backStackEntry.arguments?.getString("tvShowId")?.toIntOrNull() ?: 0
                    TvShowDetailScreen(tvShowId = tvShowId, apiKey = Constants.TMDB_API_KEY, navController)
                }


        }
    }

}