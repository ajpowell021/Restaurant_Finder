package com.adam.restaurant_finder.api

import com.adam.restaurant_finder.model.Place
import io.reactivex.Flowable


interface RemoteDataSource {

    fun getNearbyPlaces(location: String) : Flowable<List<Place>>

    fun searchRestaurants(input: String, location: String) : Flowable<List<Place>>

    fun getDetails(placeId: String) : Flowable<Place>
}
