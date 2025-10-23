package com.mustafa.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mustafa.myapplication.Home.HomeNetwork.HomeRemoteDataSourceImpl
import com.mustafa.myapplication.Home.HomeViewModel.HomeViewModel
import com.mustafa.myapplication.Home.HomeViewModel.HomeViewModelFactory
import com.mustafa.myapplication.Home.Repo.HomeRepoImpl
import com.mustafa.myapplication.network.ApiClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            HomeRepoImpl(HomeRemoteDataSourceImpl(ApiClient.api)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            viewModel.movies.collectLatest { movieList ->
                Log.d("MainActivity", "✅ Movies fetched: ${movieList.size}")
                movieList.take(5).forEach { movie ->
                    Log.d(
                        "MainActivity",
                        "🎬 ${movie.title} (${movie.releaseDate}) ⭐${movie.voteAverage}"
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Log.e("MainActivity", "❌ Error: $it")
                }
            }
        }
    }
}
