package com.mustafa.myapplication.Search

import SearchViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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


    OutlinedTextField(
         value = query,
         onValueChange = { newQuery ->
             // This sends the new text back to the ViewModel
             viewModel.onQueryChanged(newQuery)
         },
         label = { Text("Search for movies...") },
         modifier = Modifier
             .fillMaxWidth()
             .padding(16.dp),
         singleLine = true
     )




    when(state){
        is UiSearchState.Idle -> ScreenMessage("Type Something to Search !")

        is UiSearchState.Empty -> ScreenMessage("No Movies to Show !")

        is UiSearchState.Error -> ScreenMessage(
            "Error happened : ${(state as UiSearchState.Error).message}")

        is UiSearchState.Success -> {
            val movieList = (state as UiSearchState.Success).movies
            MoviesShow(movieList )
        }
        is UiSearchState.Loading -> ScreenMessage("Loading...!!")
    }

}

@Composable
fun ScreenMessage(text : String){
    Box(Modifier.fillMaxSize() , Alignment.Center){
        Text(text)
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
