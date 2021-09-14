package com.example.mvvmclean.anilshatharashi.presentation.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.example.mvvmclean.anilshatharashi.platform.networkInfo

abstract class BaseFragment : Fragment() {

    var networkUpdater: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            context.networkInfo?.isConnectedOrConnecting?.let {
                onContentAvailable()
            } ?: onContentError()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(
            networkUpdater,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(networkUpdater)
    }

    protected abstract fun onContentError()

    protected abstract fun onContentAvailable()
}
