package com.example.bingebox.Screens


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.bingebox.ui.Screens.Search.TVShowSearchListScreen

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SearchResultsScreen(apiKey: String, searchQuery: String, navController: NavController,context: Context) {
    Log.d("SearchResultsScreen", "Received query: $searchQuery")
    TVShowSearchListScreen(apiKey = apiKey, searchText = searchQuery,navController,context)
}
