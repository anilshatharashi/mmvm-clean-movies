package com.example.mvvmclean.anilshatharashi.di

import com.example.mvvmclean.anilshatharashi.BuildConfig
import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesApi
import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesRemoteDataSource
import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("content-type", "application/json;charset=utf-8")
                .build()
            it.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesMoviesApi(retrofit: Retrofit): MoviesApi =
        retrofit.create(MoviesApi::class.java)

    @Provides
    @Singleton
    fun providesMoviesRemoteDataSourceImpl(moviesApi: MoviesApi): MoviesRemoteDataSource =
        MoviesRemoteDataSourceImpl(moviesApi)

}