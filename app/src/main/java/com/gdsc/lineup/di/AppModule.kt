package com.gdsc.lineup.di

import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Nakul
 * on 26,April,2022
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSocketClient(): Socket? = IO.socket("")
}