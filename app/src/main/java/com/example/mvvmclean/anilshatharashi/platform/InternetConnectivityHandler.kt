package com.example.mvvmclean.anilshatharashi.platform

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetConnectivityHandler @Inject constructor(private val context: Context) : NetworkHandler {
     override fun isConnected(): Boolean = context.networkInfo?.isConnectedOrConnecting == true
}