package com.sol.badgenumber

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class AppLifecycleObserver : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        BadgeNumberUtils.setBadge()
    }
}