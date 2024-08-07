package com.example.bingebox.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingebox.model.TVShow
import com.example.bingebox.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class SearchViewModel(context: Context) :ViewModel(){

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText

    var SearchShows = mutableStateOf<List<TVShow>>(emptyList())
    var errorMessage = mutableStateOf<String?>(null)

    fun searchTVShows(apiKey: String, query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchTVShows(apiKey, query).awaitResponse()
                if (response.isSuccessful) {
                    val results = response.body()?.results ?: emptyList()
                    SearchShows.value = if (results.isEmpty()) {
                        errorMessage.value = "Oops we haven't got that. try searching for another shows"
                        emptyList() // Or set a specific error state
                    } else {
                        errorMessage.value = null
                        results
                    }


                } else {
                    Log.d("searchR", response.toString())
                }
            } catch (e: Exception) {

            }
        }
    }
}