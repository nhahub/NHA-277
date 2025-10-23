package com.mustafa.myapplication.Search

import SearchViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mustafa.myapplication.model.Movie

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()

   /* Search Screen
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

    */


    when(state){
        is UiSearchState.Idle -> ScreenMessage("Type Something to Search !")

        is UiSearchState.Empty -> ScreenMessage("No Movies to Show !")

        is UiSearchState.Error -> ScreenMessage(
            "Error happened : ${(state as UiSearchState.Error).message}")

        is UiSearchState.Success -> {
            val movieList = (state as UiSearchState.Success).movies
            MoviesShow(movieList )
        }
        is UiSearchState.Loading -> LoadingShow()
    }

}

@Composable
fun ScreenMessage(text : String){
    Text(text)
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
            movie ->
            Card {
                Text(movie.title ?: "not found")
            }

        }
    }
}


@Composable
fun LoadingShow(){

}