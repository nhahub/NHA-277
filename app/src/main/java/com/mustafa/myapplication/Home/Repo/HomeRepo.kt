package com.mustafa.myapplication.Home.Repo
import com.mustafa.myapplication.model.PopularResponse

interface HomeRepo {
    suspend fun getPopularMovies(page: Int): Result<PopularResponse>
}