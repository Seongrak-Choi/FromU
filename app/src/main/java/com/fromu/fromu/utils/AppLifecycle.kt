package com.fromu.fromu.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.fromu.fromu.FromUApplication.Companion.RUNNING_FLAG
import com.fromu.fromu.model.AppRunningState
import com.fromu.fromu.ui.main.MainActivity


class AppLifecycle : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        if (activity is MainActivity) {
            RUNNING_FLAG = AppRunningState.RUNNING
        }
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}