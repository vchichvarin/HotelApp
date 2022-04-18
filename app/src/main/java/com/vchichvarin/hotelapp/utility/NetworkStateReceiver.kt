package com.vchichvarin.hotelapp.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

open class NetworkStateReceiver : BroadcastReceiver() {
    private var listeners: MutableList<NetworkStateReceiverListener> = ArrayList()
    private var connected: Boolean?
    private val TAG = "NetworkStateReceiver"

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(TAG, "Intent broadcast received")
        if (intent == null || intent.extras == null) return

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
            connected = true
        } else if (intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                java.lang.Boolean.FALSE
            )
        ) {
            connected = false
        }
        notifyStateToAll()
    }


    private fun notifyStateToAll() {
        Log.i(TAG, "Notifying state to " + listeners.size + " listener(s)")
        for (eachNetworkStateReceiverListener in listeners) notifyState(
            eachNetworkStateReceiverListener
        )
    }

    private fun notifyState(networkStateReceiverListener: NetworkStateReceiverListener?) {
        if (connected == null || networkStateReceiverListener == null) return
        if (connected == true) {
            networkStateReceiverListener.networkAvailable()
        } else {
            networkStateReceiverListener.networkUnavailable()
        }
    }

    fun addListener(networkStateReceiverListener: NetworkStateReceiverListener) {
        Log.i(
            TAG,
            "addListener() - listeners.add(networkStateReceiverListener) + notifyState(networkStateReceiverListener);"
        )
        listeners.add(networkStateReceiverListener)
        notifyState(networkStateReceiverListener)
    }

    fun removeListener(networkStateReceiverListener: NetworkStateReceiverListener) {
        listeners.remove(networkStateReceiverListener)
    }

    interface NetworkStateReceiverListener {
        /**
         * Коллбэк для события включения сети
         */
        fun networkAvailable()

        /**
         * Коллбэк для события отключения сети
         */
        fun networkUnavailable()
    }

    init {
        connected = null
    }
}