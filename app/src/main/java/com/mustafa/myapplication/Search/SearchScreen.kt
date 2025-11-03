package com.mustafa.myapplication.Search

import SearchViewModel
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mustafa.myapplication.Search.SearchNetwork.SearchRemoteDaraSourceImpl
import com.mustafa.myapplication.Search.SearchRepo.SearchRepoImpl
import com.mustafa.myapplication.Search.SearchViewModel.SearchViewModelFactory
import com.mustafa.myapplication.model.Movie


@Composable
fun SearchScreen(){
    val dataSource = remember { SearchRemoteDaraSourceImpl() }
    val repo = remember { SearchRepoImpl(dataSource) }
    val viewModel : SearchViewModel = viewModel(factory = SearchViewModelFactory(repo))

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SearchBar(
        query = query,
        onQueryChange = { newQuery -> viewModel.onQueryChanged(newQuery) },
        onClearClick = { viewModel.clearSearch() })
        Spacer(modifier = Modifier.height(16.dp))


        when(state){
            is UiSearchState.Idle -> ScreenMessage("Type Something to Search..")

            is UiSearchState.Empty -> ScreenMessage("No Movies to Show!")

            is UiSearchState.Error -> ErrorScreen((state as UiSearchState.Error).message)

            is UiSearchState.Success -> {
                val movieList = (state as UiSearchState.Success).movies
                MoviesShow(movieList )
            }
            is UiSearchState.Loading -> LoadingScreen()
        }
    }

}

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String,onQueryChange:(String)->Unit,onClearClick: ()->Unit,modifier: Modifier=Modifier){
    TextField(
        value = query ,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth().height(56.dp),
        placeholder = {
            Text(
                text = "Search For a movie..",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(),
        singleLine = true
    )
}

@Composable
fun ScreenMessage(text : String){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Text(
            text=text,
            modifier = Modifier.padding(32.dp),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MoviesShow(movies : List<Movie>){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies){
                movie -> MovieCard(movie)
        }
    }
}


@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
        , onClick = {
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = movie.title?: "not found",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
                text = " ${movie.voteAverage}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}


@Composable
fun LoadingScreen(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading..",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ErrorScreen(message :String){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(36.dp)
        ) {
            Text(
                text = "Error Occurred",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
}
