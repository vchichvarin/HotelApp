package com.vchichvarin.hotelapp.domain.mapper

import com.vchichvarin.hotelapp.data.model.HotelsList
import com.vchichvarin.hotelapp.data.model.ServerHotelsList
import com.vchichvarin.hotelapp.data.model.ServerSingleHotel
import com.vchichvarin.hotelapp.data.model.SingleHotel

private fun getAvailableSuites(serverSuites: String) =
    serverSuites
        .split(":")
        .filter { it != "" }
        .map { it.toInt() }

fun ServerHotelsList.toHotelsList() =
    HotelsList(
        singleServerHotels = this,
        suitesList = getAvailableSuites(this.suites)
    )

fun ServerSingleHotel.toSingleHotel() =
    SingleHotel(
        serverSingleHotel = this,
        suitesList = getAvailableSuites(this.suites)
    )