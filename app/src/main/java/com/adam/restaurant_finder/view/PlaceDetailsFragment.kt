package com.adam.restaurant_finder.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.ViewModelFactory
import com.adam.restaurant_finder.viewModel.SearchViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class PlaceDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by activityViewModels<SearchViewModel>() { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.place_details_fragment, container, false)

        return rootView
    }
}
