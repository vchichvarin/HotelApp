package com.vchichvarin.hotelapp.ui.main

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vchichvarin.hotelapp.R
import com.vchichvarin.hotelapp.databinding.MainActivityBinding
import com.vchichvarin.hotelapp.helper.NetworkStateReceiver
import com.vchichvarin.hotelapp.helper.NetworkStateReceiver.NetworkStateReceiverListener


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
        binding.container.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HotelListFragment.newInstance())
            .commitNow()
    }

    override fun networkUnavailable() {
        binding.container.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
    }

}