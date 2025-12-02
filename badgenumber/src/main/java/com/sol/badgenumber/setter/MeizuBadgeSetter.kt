package com.sol.badgenumber.setter

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.sol.badgenumber.BadgeNumberUtils
import com.sol.badgenumber.BadgeSetter


class MeizuBadgeSetter : BadgeSetter {
    companion object {
        private const val AUTHORITY: String = "com.meizu.flyme.launcher.app_extras"

        private val CONTENT_URI: Uri = "content://$AUTHORITY/badge_extras".toUri()


        private const val KEY_PACKAGE: String = "package" // 包名 Key

        private const val KEY_CLASS: String = "class" // 类名 Key

        private const val KEY_BADGE_NUMBER: String = "badge_number" // 红点角标数量 Key


        private const val METHOD_CHANGE_BADGE: String = "change_badge" // 修改红点角标数量的方法名

        //private const val METHOD_QUERY_BADGE: String = "query_badge" // 查询红点角标数量的方法名
    }

    override fun setBadge(number: Int) {
        try {
            val extra = Bundle()
            extra.putString(KEY_PACKAGE, BadgeNumberUtils.packageName())
            extra.putString(KEY_CLASS, BadgeNumberUtils.className())
            extra.putInt(KEY_BADGE_NUMBER, number)
            BadgeNumberUtils.app.contentResolver.call(CONTENT_URI, METHOD_CHANGE_BADGE, null, extra)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}