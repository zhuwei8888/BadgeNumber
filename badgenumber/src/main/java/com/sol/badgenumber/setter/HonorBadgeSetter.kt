package com.sol.badgenumber.setter

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.sol.badgenumber.BadgeNumberUtils
import com.sol.badgenumber.BadgeSetter

class HonorBadgeSetter : BadgeSetter {
    companion object {
        private const val URI_OLD = "content://com.huawei.android.launcher.settings/badge/"
        private const val URI_NEW = "content://com.hihonor.android.launcher.settings/badge/"
    }

    override fun setBadge(number: Int) {
        val context = BadgeNumberUtils.app
        var uri: Uri? = URI_NEW.toUri()
        var type = uri?.let { context.contentResolver.getType(it) }
        if (type.isNullOrEmpty()) {
            uri = URI_OLD.toUri()
            type = context.contentResolver.getType(uri)
            if (type.isNullOrEmpty()) {
                uri = null
            }
        }
        try {
            val extra = Bundle()
            extra.putString("package", BadgeNumberUtils.packageName())
            extra.putString("class", BadgeNumberUtils.className())
            extra.putInt("badgenumber", number)
            if (uri != null) {
                context.contentResolver.call(uri, "change_badge", null, extra)
            }
        } catch (e: Exception) {
            Log.e("HonorBadgeSetter", e.message ?: "")
        }
    }
}