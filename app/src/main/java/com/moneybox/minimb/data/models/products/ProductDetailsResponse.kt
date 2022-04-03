package com.moneybox.minimb.data.models.products

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("FriendlyName")
    val friendlyName: String
)