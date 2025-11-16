package com.mustafa.myapplication.Details.Navigation

object AppNavigation {
    const val HOME = "home"
    const val DETAILS = "details/{movieId}"

    fun detailsRoute(movieId: Int) = "details/$movieId"
}