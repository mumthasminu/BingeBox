import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingebox.TVShowRepository
import com.example.bingebox.model.Genre
import com.example.bingebox.util.SharedPreferenceHelper
import com.example.bingebox.model.TVShow
import com.example.bingebox.source.remote.RetrofitInstance
import com.example.bingebox.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

@RequiresApi(Build.VERSION_CODES.M)
class TVShowViewModel(context: Context) : ViewModel() {
    private val sharedPreferencesHelper = SharedPreferenceHelper(context)

    private val repository = TVShowRepository(context)
    var _genreTVShows = MutableStateFlow<List<TVShow>>(emptyList())
    val genreTVShows: StateFlow<List<TVShow>> = _genreTVShows.asStateFlow()
    private val _genre1TVShows = MutableStateFlow<Pair<Genre, List<TVShow>>?>(null)
    val genre1TVShows: StateFlow<Pair<Genre, List<TVShow>>?> = _genre1TVShows

    private val _genre2TVShows = MutableStateFlow<Pair<Genre, List<TVShow>>?>(null)
    val genre2TVShows: StateFlow<Pair<Genre, List<TVShow>>?> = _genre2TVShows
    val selectedTvShow = mutableStateOf<TVShow?>(null)

    var tvShows = mutableStateOf<List<TVShow>>(emptyList())

        private set

    init {
        fetchPopularTVShows(Constants.TMDB_API_KEY)
      // fetchRandomGenresTVShows(Constants.TMDB_API_KEY)
        fetchTVShowsByGenres(Constants.TMDB_API_KEY)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun fetchPopularTVShows(apiKey: String) {
        viewModelScope.launch {
            val shows = repository.getPopularTVShows(apiKey)
            tvShows.value = shows
            selectOrUpdateRandomTVShow()
        }
    }
    fun fetchTVShowsByGenres(apiKey: String) {
        viewModelScope.launch {
            val genres = fetchGenresFromTMDb(apiKey)
            val randomGenres = genres.shuffled().take(2)
            val genreIds = randomGenres.map { it.id }
            _genreTVShows.value = repository.getTVShowsByGenres(apiKey,genreIds)
            if (randomGenres.isNotEmpty()) {
                val genre1Shows = fetchTVShowsByGenre(apiKey,randomGenres[0].id)
                _genre1TVShows.value = Pair(randomGenres[0], genre1Shows)
                if(randomGenres.size>1){
                    val genre2Shows = fetchTVShowsByGenre(apiKey,randomGenres[1].id)

                    _genre2TVShows.value = Pair(randomGenres[1], genre2Shows)
                }
            }
        }
    }
    private fun fetchRandomGenresTVShows(apiKey: String) {
        viewModelScope.launch {
            val genres = fetchGenresFromTMDb(apiKey)
            val randomGenres = genres.shuffled().take(2)
            if (randomGenres.isNotEmpty()) {
                val genre1Shows = fetchTVShowsByGenre(apiKey,randomGenres[0].id)
                _genre1TVShows.value = Pair(randomGenres[0], genre1Shows)
                if(randomGenres.size>1){
                    val genre2Shows = fetchTVShowsByGenre(apiKey,randomGenres[1].id)

                    _genre2TVShows.value = Pair(randomGenres[1], genre2Shows)
                }
            }



        }
    }

    private suspend fun fetchGenresFromTMDb(apiKey: String): List<Genre> {
        return try {
            val response = RetrofitInstance.api.getGenres(apiKey)
            response.genres
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchTVShowsByGenre(apiKey: String, genreId: Int): List<TVShow> {
        return try {
            val response = RetrofitInstance.api.getTVShowsByGenre(apiKey, genreId)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
    private fun selectOrUpdateRandomTVShow() {
        if (tvShows.value.isNotEmpty()) {
            val storedTvShowId = sharedPreferencesHelper.getSelectedTVShowId()
            if (storedTvShowId == -1) {
                val randomTvShow = tvShows.value.random()
                sharedPreferencesHelper.setSelectedTVShowId(randomTvShow.id)
                selectedTvShow.value = randomTvShow
            } else {
                selectedTvShow.value = tvShows.value.find { it.id == storedTvShowId }
            }
        }
    }

    fun refreshPopularTVShows(apiKey: String) {
        fetchPopularTVShows(apiKey)
    }
    fun refreshGenreTVShows(genreId: Int) {
        viewModelScope.launch {
            // Replace with your logic to refresh TV shows for the specific genre
            fetchTVShowsByGenres(apiKey = Constants.TMDB_API_KEY)
        }
    }

    fun clearTVShows() {
        tvShows.value = emptyList()
    }
    fun clearTVShow() {
        selectedTvShow.value =null
    }
    fun clearTVShowbyGenre() {
        _genreTVShows.value= emptyList()
    }
}
