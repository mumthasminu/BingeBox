package com.example.bingebox.ui.Screens

import TVShowViewModel
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bingebox.Navigation.Routes
import com.example.bingebox.R
import com.example.bingebox.ui.Screens.Home.GenreTVShowList
import com.example.bingebox.ui.Screens.Home.TVShowListScreen
import com.example.bingebox.ui.Screens.Home.popularTvShow
import com.example.bingebox.util.Constants
import com.example.bingebox.viewModel.ColorViewModel
import com.example.bingebox.viewModel.LoadingViewModel
import com.example.bingebox.viewModel.TVShowViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun HomeScreen(
    navController: NavController,
    context: Context,
    backgroundColor: Color,
    loadingViewModel: LoadingViewModel
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val colorViewModel: ColorViewModel = viewModel()
    val backgroundColor by colorViewModel.backgroundColor.collectAsState()
    val tvShowViewModel: TVShowViewModel = viewModel(factory = TVShowViewModelFactory(context))
    val genre1TVShows by tvShowViewModel.genre1TVShows.collectAsState()
    val genre2TVShows by tvShowViewModel.genre2TVShows.collectAsState()
    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing) }
    val isLoading by loadingViewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                isRefreshing = true
                kotlinx.coroutines.GlobalScope.launch {
                    delay(200)
                    isRefreshing = false
                }
            }
        ) {
            Scaffold(modifier = Modifier    .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.red) ,
                        colorResource(id = R.color.appicon_background)
                    )
                )
            ),
                topBar = {
                    TopAppBar(
                        title = {
                            Text("BingeBox")
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Routes.searchResultsScreen) }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(onClick = { /* Handle profile click */ }) {
                                Image(
                                    painter = rememberImagePainter(R.drawable.profile),
                                    contentDescription = "Profile"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            colorResource(id = R.color.red) ,
                            colorResource(id = R.color.appicon_background)
                        )
                    )
                },
                content = { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        colorResource(id = R.color.red) ,
                                        colorResource(id = R.color.appicon_background)
                                    )
                                )
                            )
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp) // Add padding around the content
                        ) {
                            item {
                                popularTvShow(navController, context = context, apiKey = Constants.TMDB_API_KEY) { color ->
                                    Log.d("ColorUpdate", "Color extracted: $color")
                                    colorViewModel.updateBackgroundColor(color)
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp)) // Spacer between Image and TVShowList
                                TVShowListScreen(apiKey = Constants.TMDB_API_KEY, searchText = "", navController = navController, context = context, loadingViewModel)
                            }
                            item {
                                genre1TVShows?.let {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    GenreTVShowList(navController, it,tvShowViewModel)
                                }
                            }
                            item {
                                genre2TVShows?.let {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    GenreTVShowList(navController, it,tvShowViewModel)
                                }
                            }
                        }
                    }
                }
            )
        }

        if (isLoading) {
            // Overlay loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

