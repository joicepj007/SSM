package com.android.ssm

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.ssm.util.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SandSMediaApplication : Application(){

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        NetworkMonitor(this).startNetworkCallback()
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onTerminate() {
        super.onTerminate()
        // Stop network callback
        NetworkMonitor(this).stopNetworkCallback()
    }


}