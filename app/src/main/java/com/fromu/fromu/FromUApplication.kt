package com.fromu.fromu

import android.app.Application
import android.content.SharedPreferences
import com.fromu.fromu.utils.Const

class FromUApplication : Application() {

    companion object {

        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = applicationContext.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }
}