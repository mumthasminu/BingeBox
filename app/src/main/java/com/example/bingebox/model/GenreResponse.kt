package com.example.bingebox.model

data class GenreResponse(val genres: List<Genre>)
data class Genre(val id: Int, val name: String)
data class TVShowResponse(val results: List<TVShow>)