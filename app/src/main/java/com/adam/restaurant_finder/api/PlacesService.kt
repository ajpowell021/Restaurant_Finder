package com.adam.restaurant_finder.api

import com.adam.restaurant_finder.model.SearchResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


interface PlacesService {

    @GET("maps/api/place/nearbysearch/json?rankby=distance&type=restaurant")
    fun getNearby(
        @Query("key") apiKey: String,
        @Query("location") location: String,
    ) : Flowable<SearchResponse>


    @GET("maps/api/place/textsearch/json?type=restaurant&inputtype=textquery&radius=15000")
    fun searchRestaurant(
        @Query("key") apiKey: String,
        @Query("input") input: String,
        @Query("location") location: String
    ) : Flowable<SearchResponse>
}
