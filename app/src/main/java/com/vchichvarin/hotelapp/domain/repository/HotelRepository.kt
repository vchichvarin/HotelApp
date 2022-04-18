package com.vchichvarin.hotelapp.domain.repository

import com.vchichvarin.hotelapp.data.utility.ServerState
import kotlinx.coroutines.flow.Flow

interface HotelRepository {

    suspend fun getHotels() : Flow<ServerState>
    suspend fun getSingleHotelInfo(hotelID: Int) : Flow<ServerState>
}