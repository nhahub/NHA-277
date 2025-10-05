package com.mustafa.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.mustafa.myapplication.Home.HomeRepo
import com.mustafa.myapplication.network.ApiClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = HomeRepo(ApiClient.api)

        lifecycleScope.launch {
            try {
                val result = repo.getPopularMovies(1)
                if (result.isSuccess) {
                    val data = result.getOrNull()
                    Log.d("MainActivity", "✅ Movies fetched: ${data?.results?.size}")
                    data?.results?.take(5)?.forEach { movie ->
                        Log.d("MainActivity", "🎬 ${movie.title} (${movie.releaseDate}) ⭐${movie.voteAverage}")
                    }
                } else {
                    Log.e("MainActivity", "❌ Failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "❌ Exception: ${e.message}")
            }
        }
    }
}
