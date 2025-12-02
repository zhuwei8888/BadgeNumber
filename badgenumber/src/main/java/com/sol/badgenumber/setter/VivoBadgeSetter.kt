package com.sol.badgenumber.setter

import android.annotation.SuppressLint
import android.content.ContentProviderClient
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri

import com.sol.badgenumber.BadgeNumberUtils
import com.sol.badgenumber.BadgeSetter
import com.sol.badgenumber.toIntSafe

import java.lang.reflect.Field

class VivoBadgeSetter : BadgeSetter {
    companion object {
        private const val TAG = "VivoBadgeSetter"
    }

    override fun setBadge(number: Int) {
        if (isOriginOS()) {
            setOriginOSBadge(number)
        } else {
            setFuntouchOSBadge(number)
        }
    }

    private fun setFuntouchOSBadge(number: Int) {
        try {
            val context = BadgeNumberUtils.app
            val intent = Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM")
            intent.putExtra("packageName", BadgeNumberUtils.packageName())
            intent.putExtra("className", BadgeNumberUtils.className())
            intent.putExtra("notificationNum", number)
            intent.addFlags(
                invokeIntconstants(
                    Intent::class.java.getCanonicalName(),
                    "FLAG_RECEIVER_INCLUDE_BACKGROUND",
                    0
                )
            )
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
        }
    }

    private fun setOriginOSBadge(number: Int) {
        val context = BadgeNumberUtils.app
        val uri = "content://com.vivo.abe.provider.launcher.notification.num".toUri()
        val extra = Bundle()
        extra.putString("package", BadgeNumberUtils.packageName())//接入的App包名
        extra.putString("class", BadgeNumberUtils.className())//接入的App class名
        extra.putInt("badgenumber", number)//目标的角标数
        /*这里一定要先使用 ContentProviderClient 建立非稳连接，
          不可以直接通过 getContentResolver()调用 call 方法，
          会有 Server 端崩溃带崩 Client 端的风险
        */
        var client: ContentProviderClient? = null
        try {
            client = context.contentResolver.acquireUnstableContentProviderClient(uri)
            if (client != null) {
                val result = client.call("change_badge", null, extra)?.getInt("result")
                Log.i(TAG, "result: $result")
            } else {
                // 调用角标接口失败或者不支持
                Log.e(TAG, "调用角标接口失败或者不支持")
            }
        } catch (e: Exception) {
            // 调用角标接口失败或者不支持
            Log.e(TAG, "e: ${e.message}")
        } finally {
            if (client != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    client.close()
                } else {
                    client.release()
                }
            }
        }
    }

    private fun invokeIntconstants(canonicalName: String?, name: String, default_value: Int): Int {
        var value = default_value
        try {
            val c = Class.forName(canonicalName)
            val field: Field = c.getField(name)
            value = field.get(c) as Int
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            return value
        }
    }

    @SuppressLint("PrivateApi")
    private fun isOriginOS(): Boolean {
        val spClass = Class.forName("android.os.SystemProperties")
        val method = spClass.getMethod("get", String::class.java, String::class.java)
        method.isAccessible = true
        val currentOsName = method.invoke(null, "ro.vivo.os.name", "") as String?
        val currentOsVersion =
            method.invoke(null, "ro.vivo.os.version", "") as String
        if (currentOsName == "Funtouch") {//内销
            if (currentOsVersion.toIntSafe() > 12.0) {
                return true
            }
        } else if (currentOsName == "vos") {//外销
            if (currentOsVersion.toIntSafe() > 2.0) {
                return true
            }
        }
        return false
    }

}