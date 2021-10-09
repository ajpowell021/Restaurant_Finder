package com.adam.restaurant_finder

import com.adam.restaurant_finder.view.MainActivity
import com.adam.restaurant_finder.view.ListFragment
import com.adam.restaurant_finder.view.MapFragment
import com.adam.restaurant_finder.view.PlaceDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidBindingModule {

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun getSearchFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun getMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun getDetailsFragment(): PlaceDetailsFragment
}
