package com.adam.restaurant_finder.model


data class Place (
    val business_status: String,
    val icon: String,
    val name: String,
    val place_id: String,
    val rating: Float,
    val photos: List<Photo>?,
    val geometry: Geometry
)

data class Geometry (
    val location: Location
        )

data class Location (
    val lat: Double,
    val lng: Double
)
