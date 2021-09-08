package com.vchichvarin.hotelapp.data.repository

import com.vchichvarin.hotelapp.helper.State
import kotlinx.coroutines.flow.Flow

interface HotelRepository {

    fun getHotels() : Flow<State>
    fun getSingleHotelInfo(hotelID: Int) : Flow<State>
}