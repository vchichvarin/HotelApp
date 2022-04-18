package com.vchichvarin.hotelapp.di

import com.vchichvarin.hotelapp.di.modules.NetworkModule
import com.vchichvarin.hotelapp.di.modules.RepositoriesModule
import com.vchichvarin.hotelapp.di.modules.ViewModelModule
import com.vchichvarin.hotelapp.presentation.HotelListFragment
import com.vchichvarin.hotelapp.presentation.bottomsheet.SingleHotelBottomSheetDialog
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, RepositoriesModule::class])
interface AppComponent {

    fun inject(fragment: HotelListFragment)
    fun inject(fragment: SingleHotelBottomSheetDialog)

}