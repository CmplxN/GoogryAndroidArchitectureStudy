package com.example.handnew04.data

data class NaverMovieResponse(
    var items: List<MovieData>
)

data class MovieData(
    val title: String,
    val link: String,
    val image: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: String
)

