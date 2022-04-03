package com.moneybox.minimb.data.di

import com.moneybox.minimb.data.networking.Networking.createService
import com.moneybox.minimb.data.repo.UserRepository
import com.moneybox.minimb.data.ui.accounts.AccountsViewModel
import com.moneybox.minimb.data.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {


    single { createService() }
    single { UserRepository(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { (token: String) -> AccountsViewModel(get(), token) }
}