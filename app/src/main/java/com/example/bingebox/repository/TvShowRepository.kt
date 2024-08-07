package com.example.bingebox

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.bingebox.source.local.AppDataBase
import com.example.bingebox.model.TVShow
import com.example.bingebox.source.remote.RetrofitInstance
import com.example.bingebox.util.NetworkUtils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class TVShowRepository(private val context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDataBase::class.java, "tv_shows.db"
    ).build()

    private val tvShowDao = db.tvShowDao()

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getPopularTVShows(apiKey: String): List<TVShow> {
        return withContext(Dispatchers.IO) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                // Network is available, fetch from remote
                try {
                    val response = RetrofitInstance.api.getPopularTVShows(apiKey).awaitResponse()
                    if (response.isSuccessful) {
                        val tvShows = response.body()?.results ?: emptyList()
                        tvShowDao.insert(tvShows) // Save to local database
                        tvShows
                    } else {
                        // Fallback to local data if remote fetch fails
                        tvShowDao.getAllTVShows().firstOrNull() ?: emptyList()
                    }
                } catch (e: Exception) {
                    // Handle the exception and fallback to local data
                    tvShowDao.getAllTVShows().firstOrNull() ?: emptyList()
                }
            } else {
                // Network is not available, fetch from local database
                tvShowDao.getAllTVShows().firstOrNull() ?: emptyList()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getTVShowsByGenres(apiKey: String, genreIds: List<Int>): List<TVShow> {
        return withContext(Dispatchers.IO) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                try {
                    val tvShows = mutableListOf<TVShow>()
                    for (genreId in genreIds) {
                        val response = RetrofitInstance.api.getTVShowsByGenre(apiKey, genreId)
                        val shows = response.results ?: emptyList()
                        tvShows.addAll(shows)
                        tvShowDao.insert(shows) // Save to local database
                    }
                    tvShows
                } catch (e: Exception) {
                    tvShowDao.getAllTVShowsByGenres(genreIds).firstOrNull() ?: emptyList()
                }
            } else {
                tvShowDao.getAllTVShowsByGenres(genreIds).firstOrNull() ?: emptyList()
            }
        }
    }


}

