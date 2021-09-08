package com.vchichvarin.hotelapp.helper

import com.vchichvarin.hotelapp.data.model.CorrectedListHotel
import com.vchichvarin.hotelapp.data.model.CorrectedSingleHotel
import com.vchichvarin.hotelapp.data.model.ListHotel
import com.vchichvarin.hotelapp.data.model.SingleHotel

sealed class State {

    class SuccessLoadedSingleHotel(val hotel: SingleHotel) : State()
    class SuccessCorrectedSingleHotel(val hotel: CorrectedSingleHotel) : State()
    class SuccessLoadedListHotel(val list: List<ListHotel>) : State()
    class SuccessCorrectedListHotel(val list: ArrayList<CorrectedListHotel>) : State()
    object ERROR : State()
    object LOADING : State()

}