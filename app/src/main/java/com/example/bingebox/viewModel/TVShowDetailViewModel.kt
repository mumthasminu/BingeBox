package com.example.bingebox.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bingebox.model.TVShow
import com.example.bingebox.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TVShowDetailViewModel(private val apiKey: String) : ViewModel() {
    private val _tvShow = MutableStateFlow<TVShow?>(null)
    val tvShow: StateFlow<TVShow?> = _tvShow.asStateFlow()

    private val api = RetrofitInstance.api

    fun loadTVShowDetails(tvShowId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = api.getTVShowDetails(tvShowId, apiKey = apiKey)
                _tvShow.value = response
            } catch (e: Exception) {
                // Log or handle the error
                Log.e("TVShowDetailViewModel", "Failed to fetch TV show details", e)
            }
        }
    }
}
//class TVShowDetailViewModel(apiKey: String) : ViewModel() {
//    private val _tvShow = MutableStateFlow<TVShow?>(null)
//    val tvShow: StateFlow<TVShow?> = _tvShow.asStateFlow()
//
//    private val api = RetrofitInstance.api
//
//    fun loadTVShowDetails(tvShowId: Int, apiKey: String) {
//        viewModelScope.launch {
//            try {
//                val response = api.getTVShowDetails(tvShowId, apiKey = apiKey)
//                _tvShow.value = response
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }
//}

class TVShowDetailViewModelFactory(private val apiKey: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TVShowDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TVShowDetailViewModel(apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




