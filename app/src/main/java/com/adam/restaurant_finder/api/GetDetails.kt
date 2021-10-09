package com.adam.restaurant_finder.api

import com.adam.restaurant_finder.model.Place
import io.reactivex.Flowable
import javax.inject.Inject


open class GetDetails @Inject constructor(private val remote: RemoteDataSource) {

    open fun execute(placeId: String): Flowable<Response> {
        return remote.getDetails(placeId).map { Response(it) }
    }

    class Request(val placeId: String)

    class Response(val place: Place)
}
