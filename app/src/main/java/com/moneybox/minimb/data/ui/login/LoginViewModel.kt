package com.moneybox.minimb.data.ui.login

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneybox.minimb.data.models.login.LoginResponse
import com.moneybox.minimb.data.repo.UserRepository
import com.moneybox.minimb.data.utility.Event
import com.moneybox.minimb.data.utility.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {


    // contains data about login
    private val _login = MutableStateFlow<Resource<LoginResponse?>>(Resource.success(null))
    val login: StateFlow<Resource<LoginResponse?>>
        get() = _login

    // holds email and pass data
    private val viewState = MutableStateFlow(LoginViewState())

    private val _navigateToAccounts = MutableSharedFlow<Event<LoginResponse>?>(1)
    val navigateToAccounts: SharedFlow<Event<LoginResponse>?> = _navigateToAccounts

    private val _error = MutableSharedFlow<Event<String>?>(1)
    val error: SharedFlow<Event<String>?> = _error

    val continueButtonEnable = viewState.combine(login) { state, login ->
        state.email.isValidEmail() && state.password.length > 5 && login.isLoading().not()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun loginButtonClicked() = viewModelScope.launch {

        _login.emit(Resource.loading())
        repository.login(viewState.value.email, viewState.value.password).collect {
            _login.value = it
            if(it.isSuccess()) it.data?.let { _navigateToAccounts.tryEmit(Event(it)) }
            if(it.isError()) it.error?.message?.let { _error.tryEmit(Event(it)) }
        }
    }

    fun emailEntered(email: String){

        viewState.value.let { lastValue ->

            viewState.tryEmit(
                lastValue.copy(email = email)
            )
        }
    }
    fun passEntered(password: String){

        viewState.value.let { lastValue ->

            viewState.tryEmit(
                lastValue.copy(password = password)
            )
        }
    }

}


data class LoginViewState(
    val email: String = "",
    val password: String = ""
)

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}