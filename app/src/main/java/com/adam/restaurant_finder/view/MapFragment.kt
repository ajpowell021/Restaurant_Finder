package com.adam.restaurant_finder.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.ViewModelFactory
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


class MapFragment: OnMapReadyCallback, GoogleMap.OnMarkerClickListener, DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by activityViewModels<SearchViewModel>() { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.map_fragment, container, false)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        map.clear()
        val userLocation: LatLng? = viewModel.getUserLocation()
        userLocation?.let {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, zoomLevel))

            val places = viewModel.searchResults.value

            places?.map { place ->
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(place.geometry.location.lat, place.geometry.location.lng))
                        .title(place.name)
                )

                map.setOnMarkerClickListener(this)
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        return false
    }

    companion object {
        const val zoomLevel = 12f
    }
}
