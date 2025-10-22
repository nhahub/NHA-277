
import androidx.lifecycle.ViewModel
import com.mustafa.myapplication.Search.SearchRepo.SearchRepo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import android.util.Log
import com.mustafa.myapplication.Search.UiSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import retrofit2.HttpException

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

    private var searchJob : Job? = null

    fun onQueryChanged(newQuery: String) {
        Log.d(TAG, "onQueryChanged: $newQuery")
        _searchQuery.value=newQuery

        if(newQuery.trim().isEmpty()){
            _uiState.value = UiSearchState.Idle
        }
    }
    @OptIn(FlowPreview::class)
    private fun setUpSearchDebounce(){
        searchQuery.debounce(300)
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

            _uiState.value = UiSearchState.Idle
            return
        }
        searchJob = viewModelScope.launch {
            try {
                Log.d(TAG, "Loading")
                _uiState.value = UiSearchState.Loading

                Log.d(TAG, "Calling API for : ${trimmedQuery}")
                val response = searchRepo.searchMovies(query = trimmedQuery)
                if (isActive) {
                    if (response.isEmpty()) {
                        Log.d(TAG, "No results found")
                        _uiState.value = UiSearchState.Empty
                    } else {
                        Log.d(TAG, "Found ${response.size}")
                        _uiState.value = UiSearchState.Success(response)
                    }
                }
        }catch (e:IOException) {
            Log.d(TAG,"Network Error")
            _uiState.value = UiSearchState.Error("Check your Network connection")
        }catch (e: HttpException){
            if (isActive){

            }

        }catch (e:Exception){
            if (isActive){
                Log.d(TAG,"Something went wrong")
                _uiState.value = UiSearchState.Error("Something went wrong")
            }
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