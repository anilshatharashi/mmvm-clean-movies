package com.example.mvvmclean.anilshatharashi.di

import android.content.Context
import com.example.mvvmclean.anilshatharashi.platform.InternetConnectivityHandler
import com.example.mvvmclean.anilshatharashi.platform.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PlatformModule {

    @Singleton
    @Provides
    fun provideInternetConnectivityHandler(@ApplicationContext context: Context): NetworkHandler =
        InternetConnectivityHandler(context)

}