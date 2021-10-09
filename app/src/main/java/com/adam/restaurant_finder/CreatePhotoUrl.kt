package com.adam.restaurant_finder

import com.adam.restaurant_finder.model.Photo

fun createPhotoUrl(photo: Photo): String {
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=${photo.width}" +
            "&photoreference=${photo.photo_reference}&key=${BuildConfig.API_KEY}"
}
