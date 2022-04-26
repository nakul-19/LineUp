package com.gdsc.lineup.di

import android.content.Context
import android.content.SharedPreferences
import com.gdsc.lineup.R
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
}
