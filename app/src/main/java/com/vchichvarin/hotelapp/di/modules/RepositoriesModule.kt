package com.vchichvarin.hotelapp.di.modules

import com.vchichvarin.hotelapp.data.repository.HotelRepositoryImpl
import com.vchichvarin.hotelapp.domain.interactor.HotelInteractor
import com.vchichvarin.hotelapp.domain.interactor.HotelInteractorImpl
import com.vchichvarin.hotelapp.domain.repository.HotelRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {

    @Binds
    fun hotelRepository(hotelRepositoryImpl: HotelRepositoryImpl) : HotelRepository

    @Binds
    fun hotelInteractor(hotelInteractorImpl: HotelInteractorImpl) : HotelInteractor

}