package com.vchichvarin.hotelapp.data.repository

import com.vchichvarin.hotelapp.api.HotelApi
import com.vchichvarin.hotelapp.data.model.ServerHotelsList
import com.vchichvarin.hotelapp.data.utility.ServerState
import com.vchichvarin.hotelapp.domain.repository.HotelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val hotelApi: HotelApi
) : HotelRepository {

    private var hotelList = mutableListOf<ServerHotelsList>()

    override suspend fun getHotels() : Flow<ServerState> {
        return flow {
            try {
                this@flow.emit(ServerState.LOADING)
                val response = hotelApi.getHotelsList()
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (i in it.indices) {
                            hotelList.add(it[i])
                        }
                        this@flow.emit(ServerState.SuccessServerStateHotelsList(hotelList))
                    }
                    hotelList.clear()
                }
                else this@flow.emit(ServerState.ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getSingleHotelInfo(hotelID: Int): Flow<ServerState> {
        return flow {
            try {
                this@flow.emit(ServerState.LOADING)
                val response = hotelApi.getHotelInfo(hotelID)
                if (response.isSuccessful) {
                    response.body()?.let {
                        this@flow.emit(ServerState.SuccessServerStateSingleHotel(it))
                    }
                }
                else this@flow.emit(ServerState.ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}