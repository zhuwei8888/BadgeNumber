package com.sol.badgenumber.setter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.sol.badgenumber.BadgeNumberUtils
import com.sol.badgenumber.BadgeSetter

class OppoBadgeSetter : BadgeSetter {
    override fun setBadge(number: Int) {
        try {
            val context = BadgeNumberUtils.app
            val endNumber: Int = if (number == 0) -1 else number
            val intent = Intent("com.oppo.unsettledevent")
            intent.putExtra("pakeageName", BadgeNumberUtils.packageName())
            intent.putExtra("number", endNumber)
            intent.putExtra("upgradeNumber", endNumber)
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent)
            } else {
                try {
                    val extras = Bundle()
                    extras.putInt("app_badge_count", endNumber)
                    context.contentResolver.call(
                        "content://com.android.badge/badge".toUri(),
                        "setAppBadgeCount",
                        null,
                        extras
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun canResolveBroadcast(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        val receivers = packageManager.queryBroadcastReceivers(intent, 0)
        return receivers.isNotEmpty()
    }

}