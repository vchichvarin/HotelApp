package com.vchichvarin.hotelapp.domain.utility

import com.vchichvarin.hotelapp.data.model.HotelsList
import com.vchichvarin.hotelapp.data.model.SingleHotel

sealed class InteractorState {

    class SuccessInteractorStateSingleHotel(val hotel: SingleHotel) : InteractorState()

    class SuccessInteractorStateHotelsList(val list: List<HotelsList>) : InteractorState()

    object ERROR : InteractorState()
    object LOADING : InteractorState()

}
