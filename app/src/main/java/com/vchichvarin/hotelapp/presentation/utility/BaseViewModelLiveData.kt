package com.vchichvarin.hotelapp.presentation.utility

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class BaseViewModelLiveData<T>(initValue: T? = null) {

    private var _liveData = MutableLiveData<T>()

    init {
        initValue?.let { _liveData.value = it }
    }

    val liveData: LiveData<T> get() = _liveData

    val value: T? get() = _liveData.value
    val valueNonNull: T get() = value!!
    val hasValue: Boolean get() = value != null

    @MainThread
    internal fun setValueNow(newValue: T) {
        _liveData.apply { value = newValue }
    }

    internal fun setValue(newValue: T) {
        _liveData.apply { postValue(newValue) }
    }
}

fun <T> BaseViewModelLiveData<T>.subscribe(
    owner: LifecycleOwner,
    observer: Observer<in T>,
) = liveData.observe(owner, observer)