package com.adam.restaurant_finder.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.ViewModelFactory
import com.adam.restaurant_finder.model.Place
import com.adam.restaurant_finder.viewModel.SearchViewModel
import dagger.android.support.DaggerFragment
import androidx.lifecycle.Observer
import com.adam.restaurant_finder.createPhotoUrl
import com.squareup.picasso.Picasso
import javax.inject.Inject


class PlaceDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by activityViewModels<SearchViewModel>() { viewModelFactory }

    private lateinit var mainImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var websiteTextView: TextView
    private lateinit var hoursTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.place_details_fragment, container, false)

        // Init UI
        mainImageView = rootView.findViewById(R.id.main_image_view)
        nameTextView = rootView.findViewById(R.id.name_text_view)
        ratingTextView = rootView.findViewById(R.id.rating_text_view)
        phoneTextView = rootView.findViewById(R.id.phone_text_view)
        addressTextView = rootView.findViewById(R.id.address_text_view)
        websiteTextView = rootView.findViewById(R.id.website_text_view)
        hoursTextView = rootView.findViewById(R.id.hours_text_view)

        val placeId = arguments?.getString(placeIdKey)
        placeId?.let {
            viewModel.getDetails(placeId)
        }

        val detailedPlaceObserver = Observer<Place> {
            setUI(it, rootView)
        }
        viewModel.detailedPlace.observe(this, detailedPlaceObserver)

        return rootView
    }

    private fun setUI(place: Place, rootView: View) {
        val mainPhoto = place.photos?.firstOrNull()
        mainPhoto?.let {
            picasso
                .load(createPhotoUrl(it))
                .into(mainImageView)
        }

        nameTextView.text = place.name
        ratingTextView.text = rootView.context.resources.getString(R.string.rating_template, place.rating.toString())
        phoneTextView.text = place.formatted_phone_number
        addressTextView.text = place.formatted_address

        if (place.website != null) {
            websiteTextView.text = place.website
        } else {
            websiteTextView.visibility = View.GONE
        }

        place.opening_hours?.let {
            hoursTextView.text = formatHoursString(place.opening_hours.weekday_text, rootView)
        }
    }

    private fun formatHoursString(hours: List<String>, rootView: View): String {
        return rootView.context.resources.getString(R.string.hours_label) +
                hours.joinToString(separator = "") { it + "\n" }
                    .replace(",","")
    }

    companion object {
        const val placeIdKey: String = "PLACE_ID"
    }
}
