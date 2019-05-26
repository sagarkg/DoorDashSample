package com.sagarganatra.doordashsample.ui.restaurantlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarganatra.doordashsample.R
import com.sagarganatra.doordashsample.models.Ad
import com.sagarganatra.doordashsample.models.Restaurant
import kotlinx.android.synthetic.main.row_ads.view.*
import kotlinx.android.synthetic.main.row_discover_list.view.*
import timber.log.Timber

class RestaurantListAdapter(
    private val context: Context,
    private val restaurants: List<Restaurant>,
    private var ad: Ad
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setAd(ad: Ad) {
        this.ad = ad
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ViewType.AD.ordinal) {
            AdViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_ads, parent, false)
            )
        } else {
            return RestaurantViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_discover_list, parent, false)
            )
        }

    }


    override fun getItemCount(): Int {
        Timber.d("${restaurants.count()}")
        return restaurants.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is RestaurantViewHolder) {
            holder.bind(context, restaurants[position])
        } else if (holder is AdViewHolder) {
            holder.bind(context, ad)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(!this.ad.isError) {
            if(position == 0) ViewType.AD.ordinal
            else ViewType.RESTAURANT.ordinal
        } else {
            ViewType.RESTAURANT.ordinal
        }

    }
}

class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(context: Context, restaurant: Restaurant) {

        itemView.nameTextView.text = restaurant.business.name
        itemView.descTextView.text = restaurant.description
        itemView.statusTextView.text = restaurant.status

        Glide.with(context)
            .load(restaurant.coverImageUrl)
            .into(itemView.imageView)
    }
}

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(context: Context, ad: Ad?) {
        itemView.adTextView.text = ad?.string
        itemView.dismissTextView.setOnClickListener {
            if(context is RestaurantListActivity)
                context.dismissAds()
        }
    }
}

enum class ViewType {
    RESTAURANT,
    AD
}