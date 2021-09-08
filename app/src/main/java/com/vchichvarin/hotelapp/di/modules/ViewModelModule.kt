package com.vchichvarin.hotelapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vchichvarin.hotelapp.di.factory.ViewModelFactory
import com.vchichvarin.hotelapp.ui.main.HotelListViewModel
import com.vchichvarin.hotelapp.ui.main.bottomsheet.SingleHotelBottomSheetViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelListViewModel::class)
    fun mainViewModel(viewModel: HotelListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SingleHotelBottomSheetViewModel::class)
    fun bottomSheetViewModel(viewModelSingleHotel: SingleHotelBottomSheetViewModel) : ViewModel

}
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)