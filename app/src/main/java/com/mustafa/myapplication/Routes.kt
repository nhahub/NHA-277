package com.mustafa.myapplication

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    object Home : Routes

    @Serializable
    object Search : Routes

    @Serializable
    data class Details (val movieId : Int) : Routes
}