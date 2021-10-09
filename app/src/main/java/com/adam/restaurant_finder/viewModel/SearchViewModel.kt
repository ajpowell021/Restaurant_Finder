package com.adam.restaurant_finder.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adam.restaurant_finder.api.GetDetails
import com.adam.restaurant_finder.api.NearbyRestaurants
import com.adam.restaurant_finder.api.SearchRestaurants
import com.adam.restaurant_finder.model.Place
import com.google.android.gms.maps.model.LatLng
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.net.ConnectException


open class SearchViewModel(
    private val nearbyRestaurants: NearbyRestaurants,
    private val searchRestaurants: SearchRestaurants,
    private val getDetails: GetDetails
    ): ViewModel() {

    private val disposables = CompositeDisposable()

    private var userLocation: LatLng? = null

    val searchQuery: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val detailedPlace: MutableLiveData<Place> by lazy {
        MutableLiveData<Place>()
    }

    val searchResults: MutableLiveData<MutableList<Place>> by lazy {
        MutableLiveData<MutableList<Place>>()
    }

    fun getNearby(location: String) {
        nearbyRestaurants.execute(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throw ConnectException("Error: No connection returned from retrofit: " + it.message) }
            .subscribe { response ->
                searchResults.value = response.places.toMutableList()
            }
            .let { disposables.add(it) }
    }

    fun searchRestaurant(input: String, location: String) {
        searchRestaurants.execute(input, location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throw ConnectException("Error: No connection returned from retrofit: " + it.message) }
            .subscribe { response ->
                searchResults.value = response.places.toMutableList()
            }
            .let { disposables.add(it) }
    }

    fun getDetails(placeId: String) {
        getDetails.execute(placeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throw ConnectException("Error: No connection returned from retrofit: " + it.message) }
            .subscribe { response ->
                detailedPlace.value = response.place
            }
            .let { disposables.add(it) }
    }

    fun getPlaceByLocation(location: LatLng): Place? {
        return searchResults.value?.firstOrNull {
            it.geometry?.location?.lat == location.latitude &&
                    it.geometry.location.lng == location.longitude
        }
    }


    fun setSearchQuery(text: String) {
        searchQuery.value = text
    }

    fun setUserLocation(lat: Double, long: Double) {
        userLocation = LatLng(lat, long)
    }

    fun getUserLocation(): LatLng? {
        return userLocation
    }
}
