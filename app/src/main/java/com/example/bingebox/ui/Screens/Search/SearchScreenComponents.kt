package com.example.bingebox.ui.Screens.Search

import TVShowViewModel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.bingebox.R
import com.example.bingebox.ui.Screens.Home.SearchBar
import com.example.bingebox.viewModel.SearchViewModel
import com.example.bingebox.viewModel.SearchViewModelFactory
import com.example.bingebox.viewModel.TVShowViewModelFactory

@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun TVShowSearchListScreen(apiKey: String, searchText: String, navController: NavController, context: Context) {
    val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory(context))
    val searchShows by remember { searchViewModel.SearchShows }
    var searchText by remember { mutableStateOf("") }
    val errorMessage by remember { searchViewModel.errorMessage }


    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            searchViewModel.searchTVShows(apiKey, searchText)
        } else {
            searchViewModel.SearchShows.value = listOf()
        }
    }

    Column(modifier = Modifier.fillMaxSize()    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                colorResource(id = R.color.red) ,
                colorResource(id = R.color.appicon_background)
            )
        )
    )) {
        LazyColumn(modifier = Modifier.fillMaxSize(),contentPadding = PaddingValues(16.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Ensure Box takes full width
                        .border(
                            width = 2.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    SearchBar(
                        searchText = searchText,
                        onSearchTextChange = { newText -> searchText = newText },
                        navController = navController,
                        onSearch = { query ->
                            searchViewModel.searchTVShows(apiKey, query)
                        }
                    )
                }
            }
            if (errorMessage != null) {
                item {
                    ErrorMessage(message = errorMessage!!)
                }
            }
            items(searchShows) { tvShow ->
                Column(modifier = Modifier.padding(8.dp).clickable{navController.navigate("TVShowDetail/${tvShow.id}")}) {
                    Image(
                        painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
                        contentDescription = tvShow.name,
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = tvShow.name)
                }
            }

        }
    }
}
@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}