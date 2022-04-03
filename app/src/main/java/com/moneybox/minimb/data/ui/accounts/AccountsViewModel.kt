package com.moneybox.minimb.data.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneybox.minimb.data.models.products.AllProductsResponse
import com.moneybox.minimb.data.repo.UserRepository
import com.moneybox.minimb.data.utility.Event
import com.moneybox.minimb.data.utility.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val repository: UserRepository,
    private val token: String
): ViewModel() {

    private val _products = MutableStateFlow<Resource<AllProductsResponse?>>(Resource.success(null))
    val products: StateFlow<Resource<AllProductsResponse?>>
        get() = _products

    private val _error = MutableSharedFlow<Event<String>?>(1)
    val error: SharedFlow<Event<String>?> = _error

    init {

        getInvestorProducts()
    }

    private fun getInvestorProducts() = viewModelScope.launch {

        _products.emit(Resource.loading())
        repository.investorProducts(token).collect {
            _products.value = it
            if(it.isError()) it.error?.toString()?.let { _error.tryEmit(Event(it)) }
        }
    }
}