package com.vchichvarin.hotelapp.presentation

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.vchichvarin.hotelapp.R
import com.vchichvarin.hotelapp.databinding.MainActivityBinding
import com.vchichvarin.hotelapp.utility.NetworkStateReceiver
import com.vchichvarin.hotelapp.utility.NetworkStateReceiver.NetworkStateReceiverListener


class HotelListActivity : AppCompatActivity(), NetworkStateReceiverListener{

    private lateinit var binding: MainActivityBinding

    private lateinit var networkStateReceiver: NetworkStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        startNetworkBroadcastReceiver(this)
    }

    override fun onResume() {
        registerNetworkBroadcastReceiver(this)
        super.onResume()
    }

    override fun onPause() {
        unregisterNetworkBroadcastReceiver(this)
        super.onPause()
    }

    private fun startNetworkBroadcastReceiver(context: Context) {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver.addListener((context as NetworkStateReceiverListener?)!!)
        registerNetworkBroadcastReceiver(context)
    }

    private fun registerNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun unregisterNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.unregisterReceiver(networkStateReceiver)
    }

    override fun networkAvailable() {
        binding.container.isVisible = true
        binding.errorLayout.isVisible = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HotelListFragment.newInstance())
            .commitNow()
    }

    override fun networkUnavailable() {
        binding.container.isVisible = false
        binding.errorLayout.isVisible = true
    }

}