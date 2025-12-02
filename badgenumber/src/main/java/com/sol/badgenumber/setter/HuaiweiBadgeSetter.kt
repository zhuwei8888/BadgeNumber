package com.sol.badgenumber.setter

import android.os.Bundle
import androidx.core.net.toUri
import com.sol.badgenumber.BadgeNumberUtils
import com.sol.badgenumber.BadgeSetter

class HuaiweiBadgeSetter : BadgeSetter {
    override fun setBadge(number: Int) {
        val context = BadgeNumberUtils.app
        val extra = Bundle()
        extra.putString("package", BadgeNumberUtils.packageName())
        extra.putString("class", BadgeNumberUtils.className())
        extra.putInt("badgenumber", number)
        context.contentResolver.call(
            "content://com.huawei.android.launcher.settings/badge/".toUri(),
            "change_badge",
            null,
            extra
        )
    }
}