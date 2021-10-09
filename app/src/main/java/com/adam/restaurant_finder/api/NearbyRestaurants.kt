package com.adam.restaurant_finder.api

import com.adam.restaurant_finder.model.Place
import io.reactivex.Flowable
import javax.inject.Inject

open class NearbyRestaurants @Inject constructor(private val remote: RemoteDataSource) {

    open fun execute(location: String): Flowable<Response> {
        return remote.getNearbyPlaces(location).map { Response(it) }
    }

    class Request()

    class Response(val places: List<Place>)
}
