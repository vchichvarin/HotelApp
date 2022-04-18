package com.vchichvarin.hotelapp.domain.interactor

import com.vchichvarin.hotelapp.domain.utility.InteractorState
import kotlinx.coroutines.flow.Flow

interface HotelInteractor {

    fun getHotelsList() : Flow<InteractorState>

    fun getSingleHotelInfo(hotelID: Int) : Flow<InteractorState>

}