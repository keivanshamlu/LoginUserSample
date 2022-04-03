package com.moneybox.minimb.data.networking

import com.moneybox.minimb.data.models.login.LoginRequest
import com.moneybox.minimb.data.models.login.LoginResponse
import com.moneybox.minimb.data.models.products.AllProductsResponse
import retrofit2.http.*

interface ApiMoneyBox {


    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("investorproducts")
    suspend fun investorproducts(
        @Header("Authorization") token: String
    ): AllProductsResponse
}