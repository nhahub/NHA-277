import androidx.lifecycle.ViewModel
import com.mustafa.myapplication.Search.SearchRepo.SearchRepo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import com.mustafa.myapplication.Search.SearchUI.UiSearchState
import com.mustafa.myapplication.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

private const val TAG = "SearchViewModel"

class SearchViewModel(private val searchRepo: SearchRepo) : ViewModel() {
    private val _uiState = MutableStateFlow<UiSearchState>(UiSearchState.Idle)
    val uiState: StateFlow<UiSearchState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery.asStateFlow()
    init {
        Log.d(TAG, "SearchViewModel init")
        setUpSearchDebounce()
    }

    private var lastResult : List<Movie> = emptyList()
    private var searchJob : Job? = null

    fun onQueryChanged(newQuery: String) {
        Log.d(TAG, "onQueryChanged: $newQuery")
        _searchQuery.value=newQuery

        if(newQuery.trim().isEmpty() && lastResult.isNotEmpty()){
            _uiState.value = UiSearchState.Success(lastResult)
        }
    }
    @OptIn(FlowPreview::class)
    private fun setUpSearchDebounce(){
        _searchQuery.debounce(500)
            .distinctUntilChanged()
            .onEach { query ->
                Log.d(TAG, "setUpSearchDebounce: $query")
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        val trimmedQuery = query.trim()
        Log.d(TAG, "performSearch: $query")

        if (trimmedQuery.isEmpty()) {
            Log.d(TAG, "performSearch: query is empty")
            if (lastResult.isNotEmpty()){
                Log.d(TAG,"Last Result")
                _uiState.value = UiSearchState.Success(lastResult)
            }else{
                Log.d(TAG,"There's no last result")
                _uiState.value = UiSearchState.Idle
            }
            return
        }

        searchJob = viewModelScope.launch(Dispatchers.IO) {

                Log.d(TAG, "Loading")
                _uiState.value = UiSearchState.Loading

                Log.d(TAG, "Calling API for : $trimmedQuery")
                val response = searchRepo.searchMovies(query = trimmedQuery)
                when(response){
                    is UiSearchState.Success -> {
                       if (response.movies.isEmpty()){
                           _uiState.emit(UiSearchState.Empty)
                       }
                        else{
                            lastResult = response.movies
                            _uiState.emit(response)
                       }
                    }
                    is UiSearchState.Error -> {
                        _uiState.emit(response)
                    }
                    else -> {}
                }
        }
    }
    fun searchMovies() {
        performSearch(_searchQuery.value)
    }
    fun clearSearch(){
        Log.d(TAG,"Clearing Search")
        searchJob?.cancel()
        _searchQuery.value= ""
        _uiState.value = UiSearchState.Idle
    }
}