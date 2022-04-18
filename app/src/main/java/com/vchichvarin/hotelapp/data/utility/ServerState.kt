package com.vchichvarin.hotelapp.data.utility

import com.vchichvarin.hotelapp.data.model.ServerHotelsList
import com.vchichvarin.hotelapp.data.model.ServerSingleHotel

sealed class ServerState {

    class SuccessServerStateSingleHotel(val serverHotel: ServerSingleHotel) : ServerState()

    class SuccessServerStateHotelsList(val serverHotelsList: List<ServerHotelsList>) : ServerState()

    object ERROR : ServerState()
    object LOADING : ServerState()

}