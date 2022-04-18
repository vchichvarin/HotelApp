package com.vchichvarin.hotelapp.api

import com.vchichvarin.hotelapp.data.model.ServerHotelsList
import com.vchichvarin.hotelapp.data.model.ServerSingleHotel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HotelApi {

    @GET("0777.json")
    suspend fun getHotelsList() : Response<List<ServerHotelsList>?>

    @GET("{hotel_id}.json")
    suspend fun getHotelInfo(@Path(value = "hotel_id") hotelId: Int) : Response<ServerSingleHotel>

}