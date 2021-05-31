package com.debts.debtstracker.injection

import com.debts.debtstracker.data.local.LocalPreferences
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.local.PreferencesSource
import com.debts.debtstracker.data.network.api.ApiClient
import com.debts.debtstracker.data.network.api.ApiService
import com.debts.debtstracker.data.repository.Repository
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.login.OnboardingViewModel
import com.debts.debtstracker.ui.main.MainViewModel
import com.debts.debtstracker.ui.main.add_debt.AddDebtViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object ApiServiceObject {
    val RETROFIT_SERVICE : ApiService by lazy {
        val apiClient = ApiClient(moshi)
        apiClient.createApi(ApiService::class.java)
    }
}

val remoteDataSourceModule = module(createdAtStart = true) {
    single { moshi }
    single { ApiServiceObject }
}

val preferencesModule = module {
    single(createdAtStart = true) { PreferencesSource(get()) }
}

val localPreferencesModule = module {
    single<LocalPreferencesInterface>(createdAtStart = true) { LocalPreferences(get()) }
}

val repositoryModule = module(createdAtStart = true) {
    single<RepositoryInterface> {
        Repository(get())
    }
}

val onBoardingViewModel = module {
    viewModel{ OnboardingViewModel(get()) }
}

val mainViewModel = module {
    viewModel { MainViewModel(get()) }
    viewModel { AddDebtViewModel(get()) }
}

val modulesList = listOf(
    remoteDataSourceModule,
    preferencesModule,
    localPreferencesModule,
    repositoryModule,
    onBoardingViewModel,
    mainViewModel
)
