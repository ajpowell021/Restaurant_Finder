package com.adam.restaurant_finder.model


data class Place (
    val name: String?,
    val place_id: String,
    val rating: Float?,
    val photos: List<Photo>?,
    val geometry: Geometry?,
    val vicinity: String?,
    val formatted_address: String?,
    val formatted_phone_number: String?,
    val website: String?,
    val opening_hours: Hours?
)

data class Geometry (
    val location: Location
        )

data class Location (
    val lat: Double,
    val lng: Double
)

data class Hours(
    val open_now: Boolean,
    val weekday_text: List<String>
)
