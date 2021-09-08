package com.vchichvarin.hotelapp.di.modules

import com.vchichvarin.hotelapp.BuildConfig
import com.vchichvarin.hotelapp.data.api.HotelApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    //private val BASE_URL = "https://raw.githubusercontent.com/iMofas/ios-android-test/master/"

    @Singleton
    @Provides
    fun providesLogger() : okhttp3.Interceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttp(logger:okhttp3.Interceptor) = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    @Singleton
    @Provides
    fun providesGson(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .baseUrl(BuildConfig.BASE_URL)
        .build()

    @Singleton
    @Provides
    fun providesJson(retrofit: Retrofit): HotelApi = retrofit.create(HotelApi::class.java)

}