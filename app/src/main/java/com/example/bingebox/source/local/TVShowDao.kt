package com.example.bingebox.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bingebox.model.TVShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TVShowDao {
    @Query("SELECT * FROM tv_show")
    fun getAllTVShows(): Flow<List<TVShow>>
    @Query("SELECT * FROM tv_show WHERE genre_id IN (:genreIds)")
    fun getAllTVShowsByGenres(genreIds: List<Int>): Flow<List<TVShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShows: List<TVShow>)
}

