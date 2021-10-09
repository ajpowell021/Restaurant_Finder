package com.adam.restaurant_finder.api

import com.adam.restaurant_finder.BuildConfig
import com.adam.restaurant_finder.model.Place
import io.reactivex.Flowable
import kotlin.Exception


class RetrofitPlacesDataSource(private val service: PlacesService) : RemoteDataSource {

    override fun getNearbyPlaces(location: String): Flowable<List<Place>> {
        return service.getNearby(BuildConfig.API_KEY, location)
            .map {
                if (it.status != "OK") {
                    throw Exception()
                } else {
                    it.results
                }
            }
    }

    override fun searchRestaurants(input: String, location: String): Flowable<List<Place>> {
        return service.searchRestaurant(BuildConfig.API_KEY, input, location)
            .map {
                if (it.status == "ZERO_RESULTS") {
                    // Handle zero results
                    throw Exception()
                } else if (it.status != "OK") {
                    throw Exception()
                } else {
                    it.results
                }
            }
    }
}
