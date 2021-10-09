package com.adam.restaurant_finder.api

import com.adam.restaurant_finder.model.Place
import io.reactivex.Flowable
import javax.inject.Inject


open class SearchRestaurants @Inject constructor(private val remote: RemoteDataSource) {

    open fun execute(input: String, location: String): Flowable<Response> {
        return remote.searchRestaurants(input, location).map { Response(it) }
    }

    class Request(val input: String, location: String)

    class Response(val places: List<Place>)
}
