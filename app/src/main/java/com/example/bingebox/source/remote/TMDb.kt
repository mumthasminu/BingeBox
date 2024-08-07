package com.example.bingebox.model


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TMDbApi {
    @GET("tv/popular")
    fun getPopularTVShows(
        @Query("api_key") apiKey: String
    ): Call<TVShowListResponse>


    @GET("search/tv")
    fun searchTVShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<TVShowListResponse>

    @GET("tv/{id}")
    suspend fun getTVShowDetails(
        @Path("id") tvShowId: Int,
        @Query("api_key") apiKey: String
    ): TVShow

    @GET("genre/tv/list")
    suspend fun getGenres(@Query("api_key") apiKey: String): GenreResponse

    @GET("discover/tv")
    suspend fun getTVShowsByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int
    ): TVShowResponse
}
