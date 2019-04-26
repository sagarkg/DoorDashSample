package com.sagarganatra.doordashsample.models

import com.google.gson.annotations.SerializedName

data class Restaurant (
    val id: Int,
    val business: Business,
    val description: String,
    @SerializedName("cover_img_url")val coverImageUrl: String,
    val status: String,
    @SerializedName("delivery_fee")val deliveryFee: String
)

data class Business (
    val id: Int,
    val name: String
)