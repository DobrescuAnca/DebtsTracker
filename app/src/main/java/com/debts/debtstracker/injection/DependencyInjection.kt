package com.debts.debtstracker.injection

import com.debts.debtstracker.data.local.LocalPreferences
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.local.PreferencesSource
import com.debts.debtstracker.data.network.api.ApiClient
import com.debts.debtstracker.data.network.api.ApiService
import com.debts.debtstracker.data.network.api.AuthorizationInterceptor
import com.debts.debtstracker.data.repository.Repository
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.login.OnboardingViewModel
import com.debts.debtstracker.ui.main.add_debt.AddDebtViewModel
import com.debts.debtstracker.ui.main.friends.UserListViewModel
import com.debts.debtstracker.ui.main.profile.ProfileViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val authorizationInterceptor = AuthorizationInterceptor("")

object ApiServiceObject {
    val RETROFIT_SERVICE : ApiService by lazy {
        val apiClient = ApiClient(moshi, authorizationInterceptor)
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
        Repository(get(), get())
    }
}

val onBoardingViewModel = module {
    viewModel{ OnboardingViewModel(get()) }
}

val userListViewModel = module {
    viewModel{ UserListViewModel() }
}
val profileViewModel = module {
    viewModel{ ProfileViewModel(get()) }
}
val addDebtViewModel = module {
    viewModel { AddDebtViewModel(get()) }
}

val modulesList = listOf(
    remoteDataSourceModule,
    preferencesModule,
    localPreferencesModule,
    repositoryModule,
    onBoardingViewModel,
    userListViewModel,
    profileViewModel,
    addDebtViewModel
)
