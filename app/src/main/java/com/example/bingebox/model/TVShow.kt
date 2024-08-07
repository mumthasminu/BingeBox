package com.example.bingebox.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tv_show")
data class TVShow(
    @PrimaryKey val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val genre_id :Int,
    val first_air_date: String? = null,
    val episode_run_time: Int? = null,
)
data class TVShowListResponse(
    val results: List<TVShow>
)
