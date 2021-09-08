package com.vchichvarin.hotelapp

import android.app.Application
import com.vchichvarin.hotelapp.di.AppComponent
import com.vchichvarin.hotelapp.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}