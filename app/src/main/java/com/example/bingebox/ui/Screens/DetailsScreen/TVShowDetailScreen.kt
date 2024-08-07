package com.example.bingebox.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bingebox.ui.Screens.DetailsScreen.TVShowDetailScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TvShowDetailScreen(
    tvShowId: Int,
    apiKey: String,
    navController: NavController,
) {
    TVShowDetailScreen(tvShowId ,apiKey = apiKey,navController)
}