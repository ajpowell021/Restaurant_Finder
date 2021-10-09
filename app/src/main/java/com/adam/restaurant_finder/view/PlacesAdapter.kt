package com.adam.restaurant_finder.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adam.restaurant_finder.BuildConfig
import com.adam.restaurant_finder.R
import com.adam.restaurant_finder.model.Photo
import com.adam.restaurant_finder.model.Place
import com.squareup.picasso.Picasso


class PlacesAdapter(private val places: List<Place>, private val picasso: Picasso) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val imageView: ImageView = view.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_list_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = places[position].name

        val photo = places[position].photos?.firstOrNull()
        photo?.let {
            picasso
                .load(getPhotoUrl(photo))
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    private fun getPhotoUrl(photo: Photo): String {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=${photo.width}" +
                "&photoreference=${photo.photo_reference}&key=${BuildConfig.API_KEY}"
    }
}
