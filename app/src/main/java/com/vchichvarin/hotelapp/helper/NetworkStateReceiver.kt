package com.vchichvarin.hotelapp.helper

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

        // Retrieve a ConnectivityManager for handling management of network connections
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Details about the currently active default data network. When connected, this network is the default route for outgoing connections
        val networkInfo = manager.activeNetworkInfo
        /**
         * NOTE: getActiveNetworkInfo() may return null when there is no default network e.g. Airplane Mode
         */
        if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
            connected = true
        } else if (intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                java.lang.Boolean.FALSE
            )
        ) {    //Boolean that indicates whether there is a complete lack of connectivity
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
            // Triggering function on the interface towards network availability
            networkStateReceiverListener.networkAvailable()
        } else {
            // Triggering function on the interface towards network being unavailable
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
         * When the connection state is changed and there is a connection, this method is called
         */
        fun networkAvailable()

        /**
         * Connection state is changed and there is not a connection, this method is called
         */
        fun networkUnavailable()
    }

    init {
        connected = null
    }
}