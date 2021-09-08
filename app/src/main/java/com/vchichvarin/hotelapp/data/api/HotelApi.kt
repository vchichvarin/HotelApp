package com.vchichvarin.hotelapp.data.api

import com.vchichvarin.hotelapp.data.model.ListHotel
import com.vchichvarin.hotelapp.data.model.SingleHotel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HotelApi {

    @GET("0777.json")
    suspend fun getHotels() : Response<List<ListHotel>?>

    @GET("{hotel_id}.json")
    suspend fun getSingleHotelInfo(@Path(value = "hotel_id") hotelId: Int) : Response<SingleHotel>

}