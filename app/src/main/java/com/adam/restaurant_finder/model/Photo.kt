package com.adam.restaurant_finder.model


data class Photo(
    val height: Int,
    val width: Int,
    val html_attributions: List<String>,
    val photo_reference: String
)
