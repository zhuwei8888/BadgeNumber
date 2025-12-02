package com.sol.badgenumber

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        BadgeNumberUtils.init(this)
    }
}