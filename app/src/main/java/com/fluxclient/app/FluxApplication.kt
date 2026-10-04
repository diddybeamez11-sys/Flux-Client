package com.fluxclient.app

import android.app.Application
import com.fluxclient.service.RelayController

class FluxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RelayController.initialize(this)
    }
}
