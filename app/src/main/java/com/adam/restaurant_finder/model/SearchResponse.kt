package com.adam.restaurant_finder.model


data class SearchResponse(
    val htmlAttributions: List<String>,
    val nextPageToken: String,
    val results: List<Place>,
    val status: String
)
