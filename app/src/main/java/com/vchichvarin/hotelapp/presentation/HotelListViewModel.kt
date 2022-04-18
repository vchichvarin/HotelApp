package com.vchichvarin.hotelapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchichvarin.hotelapp.data.model.HotelsList
import com.vchichvarin.hotelapp.domain.interactor.HotelInteractor
import com.vchichvarin.hotelapp.domain.utility.InteractorState
import com.vchichvarin.hotelapp.presentation.utility.BaseViewModelLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HotelListViewModel @Inject constructor(
    private val hotelInteractor: HotelInteractor
): ViewModel() {

    val loaderState = BaseViewModelLiveData<Boolean>()
    val recyclerViewState = BaseViewModelLiveData<Boolean>()
    val errorContainerState = BaseViewModelLiveData<Boolean>()
    val hotelsListState = BaseViewModelLiveData<List<HotelsList>>()

    init {
        if (!hotelsListState.hasValue) {
            getHotelsList()
        }
    }

    fun getHotelsList() {
        viewModelScope.launch(Dispatchers.IO) {
            hotelInteractor.getHotelsList()
                .catch { it.printStackTrace() }
                .collect { interactorState ->
                    when (interactorState) {
                        is InteractorState.ERROR -> {
                            errorContainerState.setValue(true)
                            loaderState.setValue(false)
                            recyclerViewState.setValue(false)
                        }
                        is InteractorState.LOADING -> {
                            errorContainerState.setValue(false)
                            loaderState.setValue(true)
                            recyclerViewState.setValue(false)
                        }
                        is InteractorState.SuccessInteractorStateHotelsList -> {
                            errorContainerState.setValue(false)
                            loaderState.setValue(false)
                            recyclerViewState.setValue(true)
                            hotelsListState.setValue(interactorState.list)
                        }
                    }
                }
        }
    }

    fun sortHotelsListSuites() {
        hotelsListState.value?.sortedByDescending {
            it.suitesList.size
        }?.let { hotelsListState.setValue(it) }
    }

    fun sortHotelsListDistance() {
        hotelsListState.value?.sortedByDescending {
            it.singleServerHotels.distance
        }?.let { hotelsListState.setValue(it) }
    }
}