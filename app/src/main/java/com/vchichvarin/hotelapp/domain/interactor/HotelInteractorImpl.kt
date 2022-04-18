package com.vchichvarin.hotelapp.domain.interactor

import com.vchichvarin.hotelapp.data.utility.ServerState
import com.vchichvarin.hotelapp.domain.mapper.toHotelsList
import com.vchichvarin.hotelapp.domain.mapper.toSingleHotel
import com.vchichvarin.hotelapp.domain.repository.HotelRepository
import com.vchichvarin.hotelapp.domain.utility.InteractorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HotelInteractorImpl @Inject constructor(
    private val repository: HotelRepository,
) : HotelInteractor {

    override fun getHotelsList(): Flow<InteractorState> {
        return flow {
            repository.getHotels()
                .catch { it.printStackTrace() }
                .collect { serverState ->
                    when (serverState) {
                        is ServerState.SuccessServerStateHotelsList -> {
                            val hotelsList = serverState.serverHotelsList.map { serverHotelsList ->
                                serverHotelsList.toHotelsList()
                            }
                            this@flow.emit(
                                InteractorState.SuccessInteractorStateHotelsList(
                                    hotelsList
                                )
                            )
                        }
                        is ServerState.LOADING -> { this@flow.emit(InteractorState.LOADING) }
                        else -> { this@flow.emit(InteractorState.ERROR) }
                    }
                }
        }
    }

    override fun getSingleHotelInfo(hotelID: Int): Flow<InteractorState> {
        return flow {
            repository.getSingleHotelInfo(hotelID)
                .catch { it.printStackTrace() }
                .collect { serverState ->
                    when (serverState) {
                        is ServerState.SuccessServerStateSingleHotel -> {
                            val singleHotelInfo = serverState.serverHotel.toSingleHotel()
                            this@flow.emit(
                                InteractorState.SuccessInteractorStateSingleHotel(
                                    singleHotelInfo
                                )
                            )
                        }
                        is ServerState.LOADING -> { this@flow.emit(InteractorState.LOADING) }
                        else -> { this@flow.emit(InteractorState.ERROR) }
                    }
                }
        }
    }


}