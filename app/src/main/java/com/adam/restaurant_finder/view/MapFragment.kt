package com.adam.restaurant_finder.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.ViewModelFactory
import com.adam.restaurant_finder.convertLatLngToString
import com.adam.restaurant_finder.model.Place
import com.adam.restaurant_finder.viewModel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MapFragment:
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener,
    DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by activityViewModels<SearchViewModel>() { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.map_fragment, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Views
        val searchView = rootView.findViewById<SearchView>(R.id.search_view)

        // SearchView Setup
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(input: String?): Boolean {
                if (!input.isNullOrEmpty()) {
                    viewModel.searchRestaurant(input, convertLatLngToString(viewModel.getUserLocation()!!))
                    viewModel.setSearchQuery(input)
                    hideKeyboard()
                    return true
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // Do nothing on text change
                return false
            }
        })

        // ViewModel Observer
        val placeObserver = Observer<List<Place>> {
            mapFragment.getMapAsync(this)
        }
        viewModel.searchResults.observe(viewLifecycleOwner, placeObserver)

        val queryObserver = Observer<String> {
            searchView.setQuery(it, false)
        }
        viewModel.searchQuery.observe(viewLifecycleOwner, queryObserver)

        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        map.clear()
        val userLocation: LatLng? = viewModel.getUserLocation()
        userLocation?.let {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, zoomLevel))

            val places = viewModel.searchResults.value

            places?.map { place ->
                place.geometry?.let {
                    map.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.geometry.location.lat, place.geometry.location.lng))
                            .title(place.name)
                            .snippet(requireContext().resources.getString(R.string.rating_template, place.rating.toString()))
                    )

                    map.setOnMarkerClickListener(this)
                    map.setOnInfoWindowClickListener(this)
                }
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        return false
    }

    override fun onInfoWindowClick(marker: Marker) {
        val place = viewModel.getPlaceByLocation(marker.position)
        place?.let {
            val bundle = bundleOf(PlaceDetailsFragment.placeIdKey to place.place_id)
            Navigation.findNavController(
                requireView()).navigate(R.id.action_mapFragment_to_placeDetailsFragment, bundle
            )
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        const val zoomLevel = 12f
    }
}
