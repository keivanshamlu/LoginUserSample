package com.moneybox.minimb.data.models.products

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Product")
    val product: ProductDetailsResponse,
    @SerializedName("Moneybox")
    val moneybox: Float,
    @SerializedName("PlanValue")
    val planValue: Float
)