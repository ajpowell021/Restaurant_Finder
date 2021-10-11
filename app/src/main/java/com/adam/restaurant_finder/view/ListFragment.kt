package com.adam.restaurant_finder.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.ViewModelFactory
import com.adam.restaurant_finder.convertLatLngToString
import com.adam.restaurant_finder.model.Place
import com.adam.restaurant_finder.viewModel.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.system.exitProcess


class ListFragment: DaggerFragment() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by activityViewModels<SearchViewModel>() { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.list_fragment, container, false)

        // Views
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.places_recycler_view)
        val searchView = rootView.findViewById<SearchView>(R.id.search_view)

        // RecyclerView Setup
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // SearchView Setup
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(input: String?): Boolean {
                if (!input.isNullOrEmpty()) {
                    viewModel.getUserLocation()?.let { userLocation ->
                        viewModel.searchRestaurant(input, convertLatLngToString(userLocation))
                        viewModel.setSearchQuery(input)
                        hideKeyboard()
                        return true
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // Do nothing on text change
                return false
            }
        })

        // ViewModel Observers
        val placeObserver = Observer<List<Place>> {
            recyclerView.adapter = PlacesAdapter(it, picasso, Navigation.findNavController(rootView))
        }
        viewModel.searchResults.observe(viewLifecycleOwner, placeObserver)

        val queryObserver = Observer<String> {
            searchView.setQuery(it, false)
        }
        viewModel.searchQuery.observe(viewLifecycleOwner, queryObserver)

        // Get the user's location if we haven't already.
        if (viewModel.getUserLocation() == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            getLocation()
        }

        return rootView
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermission.launch(ACCESS_COARSE_LOCATION)
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    viewModel.setUserLocation(location.latitude, location.longitude)
                    viewModel.getNearby("${location.latitude},${location.longitude}")
                }
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission was granted.
            getLocation()
        } else {
            // Permission was denied, check if we can ask again.
            val builder = AlertDialog.Builder(requireContext())
            if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                builder.setMessage(R.string.location_permission_explanation)
                    .setPositiveButton(R.string.location_enable_button_text) { _, _ -> getLocation() }
                    .setCancelable(false)
                    .create()
                    .show()
            } else {
                builder.setMessage(R.string.location_denied_explanation)
                    .setPositiveButton(R.string.global_ok) { _, _ -> exitProcess(0) }
                    .setCancelable(false)
                    .create()
                    .show()
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
