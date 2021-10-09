package com.adam.restaurant_finder

import com.adam.restaurant_finder.api.GetDetails
import com.adam.restaurant_finder.api.NearbyRestaurants
import com.adam.restaurant_finder.api.SearchRestaurants
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun nearbyRestaurants(nearbyRestaurants: NearbyRestaurants): NearbyRestaurants

    @Binds
    abstract fun searchRestaurants(searchRestaurants: SearchRestaurants): SearchRestaurants

    @Binds
    abstract fun getDetails(getDetails: GetDetails): GetDetails

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideSearchViewModelFactory(nearbyRestaurants: NearbyRestaurants, searchRestaurants: SearchRestaurants, getDetails: GetDetails) =
            ViewModelFactory(nearbyRestaurants, searchRestaurants, getDetails)
    }
}
