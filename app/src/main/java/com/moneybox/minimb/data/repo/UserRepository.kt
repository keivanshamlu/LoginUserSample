package com.moneybox.minimb.data.repo

import com.moneybox.minimb.data.models.login.LoginRequest
import com.moneybox.minimb.data.models.login.LoginResponse
import com.moneybox.minimb.data.models.products.AllProductsResponse
import com.moneybox.minimb.data.networking.ApiMoneyBox
import com.moneybox.minimb.data.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val service: ApiMoneyBox
) {


    fun login(email: String, pass: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.loading(null))
        val data = service.login(LoginRequest(email = email, password = pass))
        emit(Resource.success(data))
    }
        .flowOn(Dispatchers.IO)
        .catch {

            emit(Resource.error(it, null))
        }


    fun investorProducts(token: String): Flow<Resource<AllProductsResponse>> = flow {
        emit(Resource.loading(null))
        val data = service.investorproducts("Bearer $token")
        emit(Resource.success(data))
    }
        .flowOn(Dispatchers.IO)
        .catch {

            emit(Resource.error(it, null))
        }
}