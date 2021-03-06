package com.adam.restaurant_finder.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.createPhotoUrl
import com.adam.restaurant_finder.model.Place
import com.squareup.picasso.Picasso


class PlacesAdapter(
    private val places: List<Place>,
    private val picasso: Picasso,
    private val navController: NavController
    ) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardHolder: CardView = view.findViewById(R.id.list_card_view)
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val ratingTextView: TextView = view.findViewById(R.id.rating_text_view)
        val addressTextView: TextView = view.findViewById(R.id.address_text_view)
        val imageView: ImageView = view.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_list_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val place = places[position]

        holder.cardHolder.setOnClickListener {
            val bundle = bundleOf(PlaceDetailsFragment.placeIdKey to place.place_id)
            navController.navigate(R.id.action_listFragment_to_placeDetailsFragment, bundle)
        }

        holder.nameTextView.text = place.name
        holder.ratingTextView.text =
            holder.itemView.context.resources.getString(R.string.rating_template, place.rating.toString())


        // Depending on the type of search done, the address may be located
        // in 1 of 2 fields. We also hide the address line completely in the case
        // of Google not having an address.
        holder.addressTextView.visibility = View.VISIBLE
        holder.addressTextView.text = when {
            place.vicinity != null -> {
                place.vicinity
            }
            place.formatted_address != null -> {
                place.formatted_address
            }
            else -> {
                holder.addressTextView.visibility = View.GONE
                ""
            }
        }

        val photo = place.photos?.firstOrNull()
        photo?.let {
            picasso
                .load(createPhotoUrl(photo))
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }
}
