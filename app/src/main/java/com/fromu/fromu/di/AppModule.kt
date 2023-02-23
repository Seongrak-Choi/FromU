package com.fromu.fromu.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.fromu.fromu.utils.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Application.MODE_PRIVATE)
    }
}