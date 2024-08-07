package com.example.bingebox.ui.Screens.DetailsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bingebox.viewModel.TVShowDetailViewModel
import com.example.bingebox.viewModel.TVShowDetailViewModelFactory


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TVShowDetailScreen(tvShowId: Int, apiKey: String, navController: NavController) {
    val viewModel: TVShowDetailViewModel = viewModel(factory = TVShowDetailViewModelFactory(apiKey))
    val tvShow by viewModel.tvShow.collectAsState()

    LaunchedEffect(tvShowId) {
        viewModel.loadTVShowDetails(tvShowId, apiKey)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        when (tvShow) {
            null -> Text("Loading...")
            else -> {
                Image(
                    painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow!!.poster_path}"),
                    contentDescription = tvShow!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(bottom = 16.dp)
                )
                Text(text = tvShow!!.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = tvShow!!.overview)
                // Add more UI elements to display other details
            }
        }
    }
}

//@Composable
//fun TVShowDetailScreen(tvShowId: Int, apiKey: String) {
//    val viewModel: TVShowDetailViewModel = viewModel(factory = TVShowDetailViewModelFactory(apiKey))
//    val tvShow by viewModel.tvShow.collectAsState()
//
//    LaunchedEffect(tvShowId) {
//        viewModel.loadTVShowDetails(tvShowId,apiKey)
//    }
//
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        tvShow?.let { show ->
//            Image(
//                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${show.poster_path}"),
//                contentDescription = show.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(250.dp)
//                    .padding(bottom = 16.dp)
//            )
//            Text(text = show.name, style = MaterialTheme.typography.headlineMedium)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = show.overview)
//            // Add more UI elements to display other details
//        } ?: run {
//            Text("Loading...")
//        }
//    }
//}
//
//

