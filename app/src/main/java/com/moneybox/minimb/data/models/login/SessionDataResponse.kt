package com.moneybox.minimb.data.models.login

import com.google.gson.annotations.SerializedName

data class SessionDataResponse(
    @SerializedName("BearerToken")
    val bearerToken: String
)