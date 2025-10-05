package com.mustafa.myapplication.Home

import com.mustafa.myapplication.model.PopularResponse
import com.mustafa.myapplication.network.ApiService

class HomeRepo (private val api: ApiService) {
    suspend fun getPopularMovies(page: Int = 1): Result<PopularResponse> {
        return try {
            val response = api.getPopularMovies(page = page)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}