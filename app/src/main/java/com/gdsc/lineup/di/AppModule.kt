package com.gdsc.lineup.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import android.content.SharedPreferences
import com.gdsc.lineup.R
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import project.gdsc.zealicon22.network.NetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Karan verma
 * on 26,April,2022
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSocketClient(): Socket? = IO.socket("")

    @Provides
    @Singleton
    fun provideRetrofitClient(@ApplicationContext context: Context): Retrofit {
        val client = OkHttpClient.Builder().connectTimeout(0, TimeUnit.SECONDS).readTimeout(
            0,
            TimeUnit.SECONDS
        ).writeTimeout(0, TimeUnit.SECONDS).addNetworkInterceptor(StethoInterceptor())

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkService(retrofit: Retrofit) = retrofit.create(NetworkService::class.java)!!
  
   @Provides
   @Singleton
   fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
}
