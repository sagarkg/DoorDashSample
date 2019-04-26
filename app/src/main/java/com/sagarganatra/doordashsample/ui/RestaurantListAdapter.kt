package com.sagarganatra.doordashsample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarganatra.doordashsample.R
import com.sagarganatra.doordashsample.models.Restaurant
import kotlinx.android.synthetic.main.row_discover_list.view.*
import timber.log.Timber

class RestaurantListAdapter(
    private val context: Context,
    private val restaurants: List<Restaurant>
) : RecyclerView.Adapter<RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_discover_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        Timber.d("${restaurants.count()}")
        return restaurants.count()
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(context, restaurants[position])
    }
}

class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(context: Context, restaurant: Restaurant) {
        Timber.d("Name: ${restaurant.business.name}")
        Timber.d("Desc : ${restaurant.description}")
        Timber.d("Status : ${restaurant.status}")

        itemView.nameTextView.text = restaurant.business.name
        itemView.descTextView.text = restaurant.description
        itemView.statusTextView.text = restaurant.status

        Glide.with(context)
            .load(restaurant.coverImageUrl)
            .into(itemView.imageView)
    }
}