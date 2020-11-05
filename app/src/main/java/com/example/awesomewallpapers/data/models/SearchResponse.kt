package com.example.awesomewallpapers.data.models

data class SearchResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)