package com.example.bingebox.ui.Screens.Home

import TVShowViewModel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.bingebox.CommonComponents.SimpleTextComponent
import com.example.bingebox.CommonComponents.TVShowItem
import com.example.bingebox.Navigation.Routes
import com.example.bingebox.R
import com.example.bingebox.model.Genre
import com.example.bingebox.model.TVShow
import com.example.bingebox.util.Constants
import com.example.bingebox.viewModel.LoadingViewModel
import com.example.bingebox.viewModel.TVShowViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.*

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit, navController: NavController, onSearch: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = { newText -> onSearchTextChange(newText) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))

        ,
        trailingIcon = {
            IconButton(onClick = { onSearch(searchText) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        placeholder = { Text(text = "Search") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ), singleLine = true,
        keyboardActions = KeyboardActions(
            onSearch = {

                navController.navigate("SearchResults/${searchText}")
            }
        )

    )
}


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun TVShowListScreen(
    apiKey: String,
    searchText: String,
    navController: NavController,
    context: Context,
    loadingViewModel: LoadingViewModel,
) {
    val tvShowViewModel: TVShowViewModel = viewModel(factory = TVShowViewModelFactory(context))
    val tvShows by remember { tvShowViewModel.tvShows }
    var isRefreshing by remember { mutableStateOf(false) }
    val isLoading by loadingViewModel.isLoading.collectAsState()

    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing)
    }

    LaunchedEffect(Unit) {
        loadingViewModel.showLoading()
        delay(2000)
        loadingViewModel.hideLoading()
        tvShowViewModel.fetchPopularTVShows(apiKey)

    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            tvShowViewModel.clearTVShows()
            tvShowViewModel.refreshPopularTVShows(Constants.TMDB_API_KEY)


            GlobalScope.launch {
                delay(200)
                isRefreshing = false
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Popular TV Shows", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
            LazyRow(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tvShows) { tvShow ->
                    TVShowItem(
                        tvShow = tvShow,
                        onClick = { navController.navigate("TVShowDetail/${tvShow.id}") }
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.M)
@Composable
fun popularTvShow(
    navController: NavController,
    context: Context,
    apiKey: String,
    onColorExtracted: (Color) -> Unit
) {


    val tvShowViewModel: TVShowViewModel = viewModel(factory = TVShowViewModelFactory(context))
    val tvShow by remember { tvShowViewModel.selectedTvShow }
    val tvShows by remember { tvShowViewModel.tvShows }
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing) }
    Log.d("debug","${tvShowViewModel.selectedTvShow}")
    LaunchedEffect(Unit) {
        tvShowViewModel.fetchPopularTVShows(apiKey)
    }
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            tvShowViewModel.clearTVShow()
            tvShowViewModel.refreshPopularTVShows(Constants.TMDB_API_KEY)

            kotlinx.coroutines.GlobalScope.launch {
                delay(2000) // Simulate network call
                isRefreshing = false
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(50.dp)
                .clickable { tvShow?.id?.let { navController.navigate(Routes.DetailScreen) } }
        ) {
            tvShow?.poster_path?.let { posterPath ->
                val imageUrl = "https://image.tmdb.org/t/p/w500$posterPath"

                // Use a LaunchedEffect to handle bitmap loading and color extraction
                LaunchedEffect(imageUrl) {
                    val bitmap = loadBitmapFromUrl(imageUrl, context)
                    bitmap?.let {
                        extractDominantColor(it)?.let { color ->
                            onColorExtracted(color)
                        }
                    }
                }
                Column(
                    modifier = Modifier
                      
                ) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Random TV Show Poster",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Row ( horizontalArrangement = Arrangement.SpaceEvenly){
                        Text(text =  tvShow!!.name, color = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        tvShow?.first_air_date?.let { airDate ->
                            val year = airDate.substring(0, 4) // Extract first 4 characters (year)
                            Text(text = year, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        tvShow?.episode_run_time?.let { Text(text = it.toString(),color = Color.Black) }
                    }
                }

            }
        }
        }
    }

suspend fun loadBitmapFromUrl(imageUrl: String, context: Context): Bitmap? {
    val imageLoader = ImageLoader.Builder(context)
        .build()

    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false) // Disable hardware bitmap for easier manipulation
        .build()

    val result = imageLoader.execute(request)
    return (result.drawable as? BitmapDrawable)?.bitmap
}
suspend fun extractDominantColor(bitmap: Bitmap): Color? {
    return withContext(Dispatchers.Default) {
        val palette = Palette.from(bitmap).generate()
        palette.getDominantColor(Color.Black.toArgb()).let {
            Color(it)
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.M)
//@Composable
//fun GenreTVShowList(navController: NavController, genreWithTVShows: Pair<Genre, List<TVShow>>) {
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        Text(text = genreWithTVShows.first.name, style = MaterialTheme.typography.headlineSmall, color = Color.Yellow)
//        LazyRow {
//            items(genreWithTVShows.second) { tvShow ->
//                TVShowItem(
//                    tvShow = tvShow,
//                    onClick = { navController.navigate("TVShowDetail/${tvShow.id}") }
//                )
//            }
//        }
//    }
//}
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun GenreTVShowList(
    navController: NavController,
    genreWithTVShows: Pair<Genre, List<TVShow>>,
    tvShowViewModel: TVShowViewModel
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)



    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = genreWithTVShows.first.name,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Yellow
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {

                isRefreshing = true

                tvShowViewModel.refreshGenreTVShows(genreWithTVShows.first.id)
             }
        ) {
            LazyRow {
                items(genreWithTVShows.second) { tvShow ->
                    TVShowItem(
                        tvShow = tvShow,
                        onClick = { navController.navigate("TVShowDetail/${tvShow.id}") }
                    )
                }
            }
        }
    }
}
