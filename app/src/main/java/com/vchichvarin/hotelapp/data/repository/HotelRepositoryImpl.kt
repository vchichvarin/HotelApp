package com.vchichvarin.hotelapp.data.repository

import com.vchichvarin.hotelapp.data.api.HotelApi
import com.vchichvarin.hotelapp.data.model.ListHotel
import com.vchichvarin.hotelapp.helper.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val hotelApi: HotelApi
) : HotelRepository {

    private var hotelList = mutableListOf<ListHotel>()

    override fun getHotels() : Flow<State> {
        return flow {
            try {
                val response = hotelApi.getHotels()
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (i in it.indices) {
                            hotelList.add(it[i])
                        }
                        this@flow.emit(State.SuccessLoadedListHotel(hotelList))
                        //this@flow.emit(State.ERROR)
                    }
                    hotelList.clear()
                }
                else this@flow.emit(State.ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getSingleHotelInfo(hotelID: Int): Flow<State> {
        return flow {
            try {
                val response = hotelApi.getSingleHotelInfo(hotelID)
                if (response.isSuccessful) {
                    response.body()?.let {
                        this@flow.emit(State.SuccessLoadedSingleHotel(it))
                    }
                }
                else this@flow.emit(State.ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}