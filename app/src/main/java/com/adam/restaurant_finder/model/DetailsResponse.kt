package com.adam.restaurant_finder.model


data class DetailsResponse(
    val htmlAttributions: List<String>,
    val result: Place,
    val status: String
)
