package com.sol.badgenumber

import android.app.Application
import android.os.Build
import androidx.lifecycle.ProcessLifecycleOwner


object BadgeNumberUtils {

    internal lateinit var app: Application

    private var number = 0

    /**
     * 设置桌面角标
     * 部分手机需手动打开桌面角标开关
     * oppo需申请
     * xiaomi需在客户端进入后台时再次调用此方法
     */
    internal fun setBadge() {
        val manufacturer = Build.MANUFACTURER
        val badgeSetter = BadgeSetterFactory.createSetter(manufacturer)
        badgeSetter?.setBadge(number)
    }

    fun setBadgeNumber(number: Int) {
        this.number = number
        setBadge()
    }


    fun clearBadgeNumber() {
        setBadgeNumber(0)
    }

    fun init(context: Application) {
        app = context
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())
    }


    /**
     * 获取包名
     */
    internal fun packageName(): String {
        return app.packageName ?: ""
    }

    /**
     * 获取启动页的类名
     */
    internal fun className(): String {
        val context = app
        val launchClassName: String? =
            context.packageManager?.getLaunchIntentForPackage(context.packageName)
                ?.component?.className
        return launchClassName ?: ""
    }

}

internal fun String?.toIntSafe(def: Int = 0): Int {
    return try {
        this?.toInt() ?: def
    } catch (_: Exception) {
        0
    }
}