package com.vchichvarin.hotelapp.di.modules

import com.vchichvarin.hotelapp.data.repository.HotelRepository
import com.vchichvarin.hotelapp.data.repository.HotelRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {

    @Binds
    fun hotelRepository(hotelRepositoryImpl: HotelRepositoryImpl) : HotelRepository

}