package com.adam.restaurant_finder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adam.restaurant_finder.api.GetDetails
import com.adam.restaurant_finder.api.NearbyRestaurants
import com.adam.restaurant_finder.api.SearchRestaurants
import com.adam.restaurant_finder.viewModel.SearchViewModel
import javax.inject.Inject


class ViewModelFactory @Inject constructor(
    private val nearbyRestaurants: NearbyRestaurants,
    private val searchRestaurants: SearchRestaurants,
    private val getDetails: GetDetails
): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel> create(modelClass: Class<T>): T =
        SearchViewModel(nearbyRestaurants, searchRestaurants, getDetails) as T
}
