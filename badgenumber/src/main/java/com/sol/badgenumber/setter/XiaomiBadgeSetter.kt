package com.sol.badgenumber.setter

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.sol.badgenumber.BadgeNumberUtils
import com.sol.badgenumber.BadgeSetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class XiaomiBadgeSetter : BadgeSetter {
    private val appScope = ProcessLifecycleOwner.get().lifecycleScope

    companion object {
        private const val CHANNEL_ID: String = "011"

        private const val message_id: Int = 101
        private const val TITLE: String = "通知"
        private const val CONTENT: String = "有未读消息,请打开app查看。"

    }


    override fun setBadge(number: Int) {
        val context = BadgeNumberUtils.app
        val miuiVersionName = getMIUIVersionName()
        when (miuiVersionName) {
            "v6", "v7", "v8", "v9", "v10", "v11" -> {
                setBadgeOld(context, number)
            }

            else -> {
                setBadgeNew(context, number)
            }
        }
    }

    private fun setBadgeOld(context: Context, number: Int) {
        val notification = NotificationCompat.Builder(context, "default").build()
        try {
            val field = notification.javaClass.getDeclaredField("extraNotification")
            val extraNotification = field.get(notification)
            val method = extraNotification.javaClass.getDeclaredMethod(
                "setMessageCount",
                Int::class.javaPrimitiveType
            )
            method.invoke(extraNotification, number)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setBadgeNew(context: Context, number: Int) {
        val endNumber = if (number < 0) 0 else number
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification?
        notificationManager.cancel(message_id)
        // 兼容8.0弹出通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "channel_badge", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(true) //是否在桌面icon右上角显示小红点
            channel.lightColor = Color.RED //设置小红点颜色
            notificationManager.createNotificationChannel(channel)
        }
        try {
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(context.applicationInfo.icon)
                .setContentTitle(TITLE)
                .setContentText(CONTENT)
                .setNumber(endNumber)
                .build()
            if (endNumber > 0) {
                // 小米手机如果在应用内直接调用设置角标的方法，设置角标会不生效,因为在退出应用的时候角标会自动消除
                // 这里先退出应用，延迟3秒后再进行角标的设置，模拟在后台收到推送并更新角标的情景
                appScope.launch(Dispatchers.IO) {
                    delay(3000)
                    notificationManager.notify(message_id, notification)
                }
                notificationManager.notify(message_id, notification)
            } else {
                notificationManager.cancel(message_id)
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    // 获取MIUI版本号（如"V12"、"V13"等）
    @SuppressLint("PrivateApi")
    private fun getMIUIVersionName(): String? {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val obj: Any = clazz.newInstance()
            val versionName = clazz.getMethod("get", String::class.java)
                .invoke(obj, "ro.miui.ui.version.name") as String?
            return if (TextUtils.isEmpty(versionName)) "unknown" else versionName
        } catch (_: Exception) {
            return "unknown"
        }
    }
}