package com.location.livetracker.di

import com.location.livetracker.data.repository.LocationRepositoryImpl
import com.location.livetracker.data.repository.RoutesRepositoryImpl
import com.location.livetracker.domain.repository.LocationRepository
import com.location.livetracker.domain.repository.RoutesRepository
import com.location.livetracker.presentation.map.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::LocationRepositoryImpl) { bind<LocationRepository>() }
    viewModelOf(::LocationViewModel)
    singleOf(::RoutesRepositoryImpl) { bind<RoutesRepository>() }
}