package com.adam.restaurant_finder

import com.google.android.gms.maps.model.LatLng

fun convertLatLngToString(latLng: LatLng): String {
    return latLng.latitude.toString() + "," + latLng.longitude.toString()
}
